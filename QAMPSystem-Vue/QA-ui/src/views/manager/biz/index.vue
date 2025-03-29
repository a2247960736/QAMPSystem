<template>
  <div class="split-layout">
    <div class="left-panel">  
      <div class="file-tree-container">
        <!-- 搜索输入框，绑定搜索值，输入时触发搜索处理函数 -->
        <el-input
          v-model="searchValue"
          placeholder="搜索类别"
          @input="handleSearch"
          style="margin-bottom: 16px"
        />
        <!-- 树形组件，绑定树形数据、属性配置等，处理节点点击、展开、拖拽等事件 -->
        <el-tree
          :data="treeData"
          :props="treeProps"
          :expand-on-click-node="false"
          :draggable="false"
          :default-expand-all="true"
          :default-expanded-keys="expandedKeys"
          :auto-expand-parent="autoExpandParent"
          @node-click="handleNodeClick"
          @node-expand="handleNodeExpand"
        >
          <template #default="{ node, data }">
            <!-- 节点处于编辑状态时显示输入框 -->
            <div v-if="data.isEdit">
              <!-- 优化后的输入框 -->
              <el-input
                v-model="data.text"
                ref="inputRef"
                @focus="onInputFocus(data)"
                @blur="handleInputBlur(data)"
                @keyup.enter="handleInputBlur(data)"
                :class="{ 'input-error': data.text.trim() === '' && data.isEdit }"
              />
              <!-- 错误提示 -->
              <div v-if="data.text.trim() === '' && data.isEdit" class="error-message">文件夹名不能为空</div>
            </div>
            <!-- 节点非编辑状态时显示文本和操作下拉菜单 -->
            <div v-else>
             <!-- 高亮显示搜索匹配的文本 -->
  <el-tooltip :content="data.text" placement="top">
    <span 
      class="folder-name"
      v-html="highlightSearch(data.text)"
    ></span>
  </el-tooltip>
  
  <!-- 显示该节点下用例集的数量 -->
  <span class="case-count">
    ({{ data.caseIds ? data.caseIds.length : 0 }})
  </span>
              <!-- 操作下拉菜单，点击触发命令处理函数 -->
              <el-dropdown 
                trigger="click" 
                @command="handleMenuCommand(data, $event)"
                @click.stop.prevent="() => originalTreeData = JSON.parse(JSON.stringify(treeData))"
              >
                <el-icon :size="16" style="margin-left:8px;cursor:pointer">
                  <MoreFilled />
                </el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <!-- 添加同级文件夹操作 -->
                    <el-dropdown-item 
                      v-if="data.id !== 'root'" 
                      command="addSibling"
                    >添加同级文件夹</el-dropdown-item>
                    <!-- 添加子文件夹操作 -->
                    <el-dropdown-item 
                      v-if="data.id !== '-1'" 
                      command="addChild"
                    >添加子文件夹</el-dropdown-item>
                    <!-- 重命名操作 -->
                    <el-dropdown-item 
                      v-if="data.id !== '-1'" 
                      command="rename"
                    >重命名</el-dropdown-item>
                    <!-- 删除操作 -->
                    <el-dropdown-item 
                      v-if="data.id !== '-1' && data.id !== 'root'" 
                      command="delete"
                    >删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>

            <!-- 另一个操作下拉菜单，保持原有下拉菜单内容不变 -->
            <el-dropdown @command="handleMenuCommand(data, $event)">
              <i class="el-icon-more" style="margin-left: 8px"></i>
            </el-dropdown>
          </template>
        </el-tree>
      </div>
     
    </div>
        <div class="right-panel">
          <!-- <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      
      <el-form-item label="用例名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入用例名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      
      <el-form-item label="用例创建人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入用例创建人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      
      
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['manager:case:add']"
        >新建用例集</el-button>
      </el-col>
      
      
     
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

   <el-table
  v-loading="loading"
  :data="caseList"
  @selection-change="handleSelectionChange"
  @expand-change="handleExpand"
  row-key="id" 
>
  <el-table-column type="expand" width="40">
    <template #default="{ row }">
    <div >
      <el-table 
        v-loading="recordLoading" 
        :data="recordList[row.id]" 
        style="width: 100%;margin: 10px 0;background:#f8f9fa;"
      >
       
        <el-table-column label="执行计划" align="center" prop="title" width="80"/>
        <!-- <el-table-column label="执行计划" width="120" align="center">
          <template #default="scope">
            <el-link type="primary" @click="handleTaskClick(scope.row)">
              {{ scope.row.title }}
            </el-link>
          </template>
        </el-table-column> -->
        <el-table-column label="负责人" prop="owner" align="center"/>
        <el-table-column label="执行人" prop="executors" align="center"/>
        <el-table-column label="通过率" width="100" align="center">
          <template #default="scope">
            {{ scope.row.totalNum > 0 ? 
              Math.round((scope.row.successNum / scope.row.totalNum) * 100) + '%' : 
              'N/A' }}
          </template>
        </el-table-column>
        <!-- 新增已测用例集列 -->
        <el-table-column label="已测用例集" width="130" align="center">
          <template #default="scope">
            <span class="table-operation">
              {{ scope.row.executeNum || 0 }} / {{ scope.row.totalNum || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="期望时间" width="120" align="center">
          <template #default="scope">
            {{ parseTime(scope.row.expectStartTime, '{y}-{m}-{d}') }} 至 
            {{ parseTime(scope.row.expectEndTime, '{y}-{m}-{d}') }}
          </template>
        </el-table-column>
<el-table-column label="操作" align="center" width="150">
          <template #default="scope">
            <el-tooltip content="修改任务" effect="dark" placement="top">
              <el-button 
                link 
                type="primary" 
                :icon="Edit" 
                @click="TaskHandleUpdate(scope.row)"
                class="action-btn"
              />
            </el-tooltip>
            
            <el-tooltip content="执行测试" effect="dark" placement="top">
              <el-button 
                link 
                type="success" 
                :icon="VideoPlay" 
                @click="locateExectUrl(scope.row)"
                class="action-btn"
              />
            </el-tooltip>
            
            <el-tooltip content="删除任务" effect="dark" placement="top">
              <el-button 
                link 
                type="danger" 
                :icon="Delete" 
                @click="handleDelete(scope.row)"
                class="action-btn"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
</el-table-column>
      <el-table-column label="用例集ID" align="center" prop="id" />
      <el-table-column label="用例集名称" align="center" prop="title" />
     <el-table-column label="关联计划" align="center" width="200">
      <template #default="{ row }">
        <!-- 从planList中找到对应的计划，然后展示planName -->
        {{ getPlanNameByPlanId(row.planId) }}
      </template>
    </el-table-column>
      <el-table-column label="最近更新人" align="center" prop="modifier" />
      <el-table-column label="创建人" align="center" prop="creator" />
      <el-table-column label="创建时间" align="center" prop="gmtCreated" width="150">
        <template #default="scope">
          <span>{{ parseTime(scope.row.gmtCreated, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      
       <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
           <!-- 新增查看按钮 -->
        <el-tooltip content="查看" effect="dark" placement="top">
            <el-button
                link
                type="info"
                :icon="View"
                @click="locateCaseUrl(scope.row)"
                v-hasPermi="['manager:case:view']"
                class="action-btn"
            />
        </el-tooltip>

          <el-tooltip content="修改用例集" effect="dark" placement="top">
            <el-button 
              link 
              type="primary" 
              :icon="Edit" 
              @click="handleUpdate(scope.row)" 
              v-hasPermi="['manager:case:edit']"
              class="action-btn"
            />
          </el-tooltip>
          
          <el-tooltip content="创建执行任务" effect="dark" placement="top">
            <el-button 
              link 
              type="success" 
              :icon="Plus" 
              @click="TaskHandleAdd(scope.row)" 
              v-hasPermi="['manager:case:edit']"
              class="action-btn"
            />
          </el-tooltip>
          
          <el-tooltip content="删除用例集" effect="dark" placement="top">
            <el-button 
              link 
              type="danger" 
              :icon="Delete" 
              @click="handleDelete(scope.row)"
              v-hasPermi="['manager:case:remove']"
              class="action-btn"
            />
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改测试用例对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="caseRef" :model="form" :rules="rules" label-width="80px">
        
        <el-form-item label="用例名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入用例名称" />
        </el-form-item>
        <el-form ref="formRef" :model="form">
    <el-form-item label="关联计划" prop="planId">
      <el-select v-model="form.planId" placeholder="请选择关联计划" style="width: 100%">
        <el-option
          v-for="plan in planList"
          :key="plan.id"
          :label="plan.planName"
          :value="plan.planId"
        />
      </el-select>
    </el-form-item>
  </el-form>
        <el-form-item label="用例集分类" prop="bizId">
          <el-select 
            v-model="form.bizId" 
            placeholder="请选择用例集分类"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in flatTreeData"
              :key="item.id"
              :label="item.text"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
          
       <!-- 添加或修改用例执行记录对话框 -->
  <el-dialog :title="TaskTitle" v-model="TaskOpen" width="500px" append-to-body>
    <el-form ref="TaskRecordRef" :model="TaskForm" :rules="TaskRules" label-width="80px">
      <el-form-item label="用例名称" prop="title">
        <el-input v-model="TaskForm.title" placeholder="请输入用例名称" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="TaskForm.description" type="textarea" placeholder="请输入内容" />
      </el-form-item>
      <el-form-item label="选择用例集">
        <el-radio-group v-model="radioValue">
          <el-radio label="0">包含全部用例</el-radio>
          <el-radio label="1">手动圈选用例集</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="优先级" v-if="radioValue === '1'">
        <el-select v-model="selectValue" multiple placeholder="用例等级">
          <el-option v-for="(item, index) in TaskGrade" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签" v-if="radioValue === '1'">
        <el-select v-model="selectValue" multiple placeholder="请选择标签">
          <el-option v-for="(item, index) in TaskCaseInfo.taglist" :key="index" :label="item" :value="item"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="计划周期">
        <el-date-picker
          v-model="choiseDate"
          type="daterange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="负责人" prop="owner">
        <el-input v-model="TaskForm.owner" placeholder="请输入负责人" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="TaskSubmitForm">确 定</el-button>
        <el-button @click="TaskCancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
        </div>
  </div>
  

  
</template>

<script setup>
import { ref, reactive, onMounted, watch, nextTick } from 'vue';
// 导入业务接口
import { listBiz, getBiz, delBiz, addBiz, updateBiz, renameDir, deleteDir, getDirCardTree, moveDir } from "@/api/manager/biz";
// 导入Element Plus消息提示和确认框组件
import { ElMessage, ElMessageBox } from 'element-plus';
// 导入Element Plus图标
import { MoreFilled, Edit, VideoPlay, Delete, Plus,View  } from '@element-plus/icons-vue'

// 定义组件接收的属性
const props = defineProps({
  productLineId: {
    type: Number,
    required: true
  },
  getCaseList: {
    type: Function,
    required: true
  },
  getTreeList: {
    type: Function,
    required: true
  },
  treeData: {
    type: Array,
    required: true
  }
});

// 搜索输入框的值
const searchValue = ref('');
// 树形组件默认展开的节点ID数组
const expandedKeys = ref(['root']);
// 是否自动展开父节点
const autoExpandParent = ref(true);
// 原始树形数据备份
const originalTreeData = ref([]);
// 树形节点数据列表
const dataList = ref([]);
// 树形组件使用的树形数据
const treeData = ref([]);
// 输入框的引用
const inputRef = ref(null);

// 树形组件的属性配置
const treeProps = {
  children: 'children',
  label: 'text'
};
// 修改后的扁平化树数据计算属性
const flatTreeData = computed(() => {
  const flatten = (nodes) => {
    return nodes.reduce((acc, node) => {
      // 仅过滤掉根节点，保留-1
      if(node.id !== 'root') {
        acc.push({ id: node.id, text: node.text })
      }
      if(node.children) {
        acc.push(...flatten(node.children))
      }
      return acc
    }, [])
  }
  return flatten(treeData.value)
})
/**
 * 组件挂载时执行的生命周期钩子
 * 异步请求业务数据，更新树形数据并初始化展开节点
 */
onMounted(async () => {
  try {
    // 调用接口获取业务数据
     const response = await listBiz();
    if (response.code === 200) {
      // 获取响应中的树形数据
      const newTreeData = response.data.children;
      // 更新树形数据
      updateTreeData(newTreeData);
      // 初始化展开节点
      initTreeExpandedKeys(false);
    } else {
      // 显示错误消息
      ElMessage.error(response.msg);
    }
  } catch (error) {
    // 显示请求出错消息
    ElMessage.error('请求出错，请稍后重试');
  }
});





/**
 * 监听props.treeData的变化
 * 当props.treeData变化时，更新树形数据并初始化展开节点
 */
watch(() => props.treeData, (newTreeData) => {
  // 更新树形数据
  updateTreeData(newTreeData);
  // 初始化展开节点
  initTreeExpandedKeys(true);
});

/**
 * 更新树形数据
 * @param {Array} newTreeData - 新的树形数据
 */
const updateTreeData = (newTreeData) => {
  // 深拷贝新的树形数据
  treeData.value = JSON.parse(JSON.stringify(newTreeData));
};

/**
 * 初始化展开节点
 * @param {boolean} expandedAll - 是否展开所有节点
 */
const initTreeExpandedKeys = (expandedAll) => {
  // 清空数据列表
  dataList.value = [];
  // 生成数据列表
  generateList(treeData.value);
  // 根据expandedAll决定展开节点
  expandedKeys.value = expandedAll
    ? dataList.value.map(item => item.id)
    : expandedKeys.value;
};

/**
 * 生成数据列表
 * @param {Array} data - 树形数据
 * @param {Object} excludeItem - 排除的节点
 */
const generateList = (data, excludeItem = {}) => {
  for (let i = 0; i < data.length; i++) {
    const node = data[i];
    const id = node.id;
    if (excludeItem.id!== node.id) {
      // 将节点ID和文本添加到数据列表
      dataList.value.push({ id, text: node.text });
    }
    if (node.children) {
      // 递归处理子节点
      generateList(node.children);
    }
  }
};

/**
 * 处理搜索输入事件
 * 根据搜索值更新展开节点
 */
const handleSearch = () => {
  const newExpandedKeys = dataList.value
    .map(item => {
      if (item.text.indexOf(searchValue.value) > -1) {
        // 获取匹配节点的父节点ID
        return getParentKey(item.text, treeData.value);
      }
      return null;
    })
    .filter((item, i, self) => item && self.indexOf(item) === i);
  // 更新展开节点
  expandedKeys.value = newExpandedKeys;
  // 自动展开父节点
  autoExpandParent.value = true;
};

/**
 * 获取节点的父节点ID
 * @param {string} title - 节点的文本
 * @param {Array} tree - 树形数据
 * @returns {string|null} - 父节点ID或null
 */
const getParentKey = (title, tree) => {
  let parentKey;
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i];
    if (node.children) {
      if (node.children.some(item => item.text === title)) {
        // 找到匹配节点的父节点ID
        parentKey = node.id;
      } else if (getParentKey(title, node.children)) {
        // 递归查找父节点ID
        parentKey = getParentKey(title, node.children);
      }
    }
  }
  return parentKey;
};

/**
 * 处理节点点击事件
 * 调用父组件的getCaseList函数获取用例列表
 * @param {Object} data - 点击的节点数据
 */
const bizId = ref(null);
//默认
bizId.value = "root";
const handleNodeClick = (data) => {
  // if (props.getCaseList && typeof props.getCaseList === 'function') {
  //   // 调用父组件的getCaseList函数
  //   props.getCaseList([data.id])
  // } else {
  //   // 输出错误信息
  //   console.error('getCaseList prop is not a valid function')
  // }
  bizId.value = data.id;
  getList();
}

/**
 * 处理节点展开事件
 * 更新展开节点并关闭自动展开父节点
 * @param {Array} expandedKeys - 展开的节点ID数组
 */
const handleNodeExpand = (expandedKeys) => {
  // 更新展开节点
  expandedKeys.value = expandedKeys;
  // 关闭自动展开父节点
  autoExpandParent.value = false;
};

/**
 * 处理下拉菜单命令事件
 * 根据命令执行相应的操作
 * @param {Object} data - 节点数据
 * @param {string} command - 命令名称
 */
const handleMenuCommand = (data, command) => {
  switch(command) {
    case 'addSibling':
      // 添加同级文件夹
      addSibling(data)
      break
    case 'addChild':
      // 添加子文件夹
      addChild(data)
      break
    case 'rename':
      // 重命名文件夹
      rename(data)
      break
    case 'delete':
      // 删除文件夹
      isDelete(data)
      break
  }
}
// 新增变量，用于记录最新添加节点的 id
const newlyAddedNodeId = ref(null);
/**
 * 添加节点
 * @param {string} key - 目标节点ID
 * @param {Array} data - 树形数据
 * @returns {Array} - 更新后的树形数据
 */
const addNode = (key, data) => {
  return data.map(item => {
    const node = { ...item };
    if (key === item.id) {
        const newNodeId = Date.now();
      newlyAddedNodeId.value = newNodeId;
      return {
        ...item,
        children: [
          ...(item.children || []),
          {
            parentId: item.id,
            id: newNodeId, // 使用时间戳避免ID重复
            text: '',
            children: [],
            isEdit: true
            
          }
        ]
      };
    }
    if (node.children) {
      // 递归处理子节点
      node.children = addNode(key, node.children);
    }
    return node;
  });
};

/**
 * 添加同级文件夹
 * @param {Object} item - 节点数据
 */
const addSibling = (item) => {
  // 更新树形数据，添加同级文件夹
  treeData.value = addNode(item.parentId, treeData.value);
   // 新增聚焦逻辑
  nextTick(() => {
    selectInputText();
  });
};

/**
 * 添加子文件夹
 * @param {Object} item - 节点数据
 */
const addChild = (item) => {
  // 更新树形数据，添加子文件夹
  treeData.value = addNode(item.id, treeData.value);
  // 保持父节点展开状态
  nextTick(() => {
    selectInputText(); // 新增聚焦
    // 添加父节点ID和当前节点ID到展开列表
    expandedKeys.value = Array.from(new Set([
      ...expandedKeys.value,
      item.parentId,
      item.id
    ]));
    // 自动展开父节点
    autoExpandParent.value = true;
  });
};

/**
 * 重命名文件夹
 * @param {Object} item - 节点数据
 */
const originText = ref(null);
const rename = (item) => {
  originText.value = item.text;
  // 编辑节点，进入编辑状态
  editNode(item.id, treeData.value);
   // 添加 nextTick 确保编辑状态生效
  nextTick(() => {
    selectInputText();
  });
};

/**
 * 编辑节点状态
 * @param {string|null} key - 目标节点ID，为null时清除所有编辑状态
 * @param {Array} data - 树形数据
 */
const editNode = (key, data) => {
  data.forEach(item => {
    if (key === null) {
      // 清除所有编辑状态
      item.isEdit = false;
      item.rename = false;
    } else {
      if (item.id === key) {
        // 进入编辑状态
        item.isEdit = true;
        item.rename = true;
      } else {
        // 退出编辑状态
        item.isEdit = false;
        item.rename = false;
      }
    }
    if (item.children) {
      // 递归处理子节点
      editNode(key, item.children);
    }
  });
};

/**
 * 确认删除文件夹
 * @param {Object} item - 节点数据
 */
const isDelete = (item) => {
  // 显示确认框
  ElMessageBox.confirm(
    `确认删除文件夹吗？删除 ${item.text} 文件夹时，同时会删除包含的 ${item.caseIds ? item.caseIds.length : 0} 个用例集`,
    '提示',
    {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 确认删除，调用删除文件夹函数
    deleteFolder(item);
    treeData.value = removeNodeById(treeData.value, item.id);

  }).catch(() => {});
};

// 新增 removeNodeById 函数，根据 id 删除节点
const removeNodeById = (data, targetId) => {
  return data.filter(node => {
    if (node.id === targetId) {
      return false;
    }
    if (node.children) {
      node.children = removeNodeById(node.children, targetId);
    }
    return true;
  });
};
/**
 * 删除文件夹
 * @param {Object} item - 节点数据
 */
const deleteFolder = (item) => {
  // 调用接口删除文件夹
  deleteDir({
    parentId: item.parentId,
    productLineId: props.productLineId,
    delId: item.id,
    channel: 1
  }).then((res) => {
    if (res.code === 200) {
       // 直接从 treeData 中删除节点
    
      // 显示删除成功消息
      ElMessage.success('删除成功');
      // 强制刷新树数据
      // props.getTreeList(true);
      // 退出编辑模式
      exitEditMode();
    } else {
      // 显示错误消息
      ElMessage.error(res.msg);
    }
  });
};

/**
 * 处理输入框失去焦点事件
 * @param {Object} item - 节点数据
 */
const handleInputBlur = (item) => {
 
    if (item.rename) {
      // 重命名操作
      renameInput(item);
      originText.value = null;
    } else {
     
      onblurInput(item);
    }
  // 立即退出编辑模式
  editNode(null, treeData.value);
};

/**
 * 处理输入框失去焦点且为添加操作
 * @param {Object} item - 节点数据
 */
const onblurInput = (item) => {
  if (item.text === '' || item.text.trim() === '') {
    // 输入为空，显示错误消息
    // 删除最新添加的节点
    treeData.value = removeNewlyAddedNode(treeData.value, newlyAddedNodeId.value);
    newlyAddedNodeId.value = null; // 重置最新添加节点的 id
    return ElMessage.error('文件夹名称不能为空');
  }
  // 调用接口添加文件夹
  addBiz({
    parentId: item.parentId,
    productLineId: props.productLineId,
    text: item.text,
    channel: 1
  }).then((res) => {
    if (res.code === 200) {
      // 显示添加成功消息
      ElMessage.success('添加成功');
      // 重新获取数据
      // props.getTreeList();
      // 退出编辑模式
      exitEditMode();
    } else {
      // 显示错误消息
            console.log('添加失败', newlyAddedNodeId.value);
     
      ElMessage.error(res.msg);
      
    }
  }).catch((error) => {
     console.error('接口请求出错:', error);
    treeData.value = removeNewlyAddedNode(treeData.value, newlyAddedNodeId.value);
    newlyAddedNodeId.value = null; // 重置最新添加节点的 id
  });
};

// 修改 removeNewlyAddedNode 函数，根据 id 删除节点
const removeNewlyAddedNode = (data, targetId) => {
  return data.filter(node => {
    if (node.id === targetId) {
      return false;
    }
    if (node.children) {
      node.children = removeNewlyAddedNode(node.children, targetId);
    }
    return true;
  });
};


/**
 * 处理输入框失去焦点且为重命名操作
 * @param {Object} item - 节点数据
 */
const renameInput = (item) => {
   if (item.text === '' || item.text.trim() === '') {
    // 输入为空，显示错误消息
    // 删除最新添加的节点
    item.text = originText.value;
    originText.value = null;
    return ElMessage.error('重命名文件夹名不能为空');
  }
  
  // 调用接口重命名文件夹
  renameDir({
    id: item.id,
    productLineId: props.productLineId,
    text: item.text,
    channel: 1
  }).then((res) => {
    if (res.code === 200) {
      // 显示重命名成功消息
      ElMessage.success('重命名成功');
      // 重新获取数据
      // props.getTreeList().then(() => {
      //   // 退出编辑模式
      //   exitEditMode();
      //   // 保持父节点展开
      //   expandedKeys.value = Array.from(new Set([...expandedKeys.value, item.parentId]));
      //   // 自动展开父节点
      //   autoExpandParent.value = true;
      // });
    } else {
      // 退出编辑模式
      editNode(null, treeData.value);
      // 显示错误消息
      ElMessage.error(res.msg);
    }
  });
};

/**
 * 退出编辑模式
 * 将所有节点的编辑状态设置为false
 */
const exitEditMode = () => {
  treeData.value = treeData.value.map(node => {
    return {...node, isEdit: false};
  });
};

const highlightSearch = (text) => {
  if (searchValue.value) {
    const regex = new RegExp(searchValue.value, 'gi');
    return text.replace(regex, (match) => `<span style="color: #f50">${match}</span>`);
  }
  return text;
};

const handleDrop = (info) => {
  console.log('拖拽事件触发:', info); // 添加调试日志
  

  const dropNode = info.node.data;
  const dragNode = info.dragNode.data;
  if (dragNode.id === '-1') {
    ElMessage.error('未分类用例集不可移动！');
  } else if (dropNode.id === '-1') {
    ElMessage.error('未分类用例集下不可有其他文件！');
  } else {
    const fromId = dragNode.id;
    let toId = '';
    if (info.dropToGap) {
      toId = dropNode.parentId;
    } else {
      toId = dropNode.id;
    }
    moveDir({
      productLineId: props.productLineId,
      fromId,
      toId,
      channel: 1
    }).then((res) => {
      if (res.code === 200) {
        ElMessage.success('移动文件夹成功！');
        props.getTreeList();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
};

const selectInputText = () => {
  nextTick(() => {
    const inputs = document.querySelectorAll('.el-tree-node__content .el-input__wrapper input');
    if (inputs.length > 0) {
      const lastInput = inputs[inputs.length - 1];
      lastInput.focus();
      lastInput.select();
    }
  });
};
// 在 setup 区域添加以下代码
const operationDialogVisible = ref(false)
const activeNodeData = ref(null)

const showOperationDialog = (data) => {
  activeNodeData.value = data
  operationDialogVisible.value = true
}





//right区域 
/**
 * 测用例列表
 */
// import { ref, onMounted, watch } from 'vue';
import { listCase, getCase, delCase, addCase, updateCase, getCaseGeneralInfo, getCountByCondition, 
  listCreators, getCaseDetail, deleteCaseById, editCase, createOrCopyCase
 } from "@/api/manager/case";

// 状态管理
const list = ref([]);
const total = ref(0);
const record = ref({});
const title = ref('');
const visible = ref(false);
const iterationList = ref([]);
const showFilterBox = ref(false);
const productMember = ref([]);
const currCase = ref(null);
const showAddRecord = ref(false);
const envList = ref([]);
const options = ref({ projectLs: [], requirementLs: [] });
const requirement = ref(null);
const filterStatus = ref('filter-hide');
const filterVisble = ref(false);
const loading = ref(true);
const current = ref(1);
const productLineId = ref('');
// const treeData = ref([]);
const levelId = ref('');
const levelText = ref('');
// const searchValue = ref('');
// const autoExpandParent = ref(true);
// const dataList = ref([]);
const caseIds = ref(['root']);
const isSelect = ref(true);
const isSibling = ref(true);
const isAdd = ref(true);
const isReName = ref(true);
const treeSelect = ref([]);
const cardTree = ref([]);
const bizIds = ref([]);
const operate = ref('');
const formData = ref({
  case: '',
  requirementId: '',
  bizId: [],
  description: '',
});
const fileList = ref([]);
const uploadUrl = ref('');
const formRef = ref(null);
const { proxy } = getCurrentInstance();
const caseList = ref([]);
const open = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    groupId: null,
    title: null,
    description: null,
    isDelete: null,
    creator: null,
    modifier: null,
    caseContent: null,
    gmtCreated: null,
    gmtModified: null,
    extra: null,
    productLineId: null,
  },
  rules: {
    title: [
      { required: true, message: "用例名称不能为空", trigger: "blur" }
    ],
    bizId: [
      { required: true, message: "用例集分类不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

function getCaseList(data){
  //打印调试
  console.log(data);

}
// 获取测试用例列表
// const getCaseList = async (
//   currentPage,
//   nameFilter,
//   createrFilter,
//   iterationFilter,
//   choiseDate = [],
//   caseKeyWords = ''
// ) => {
//   try {
//     const res = await listCase({
//       pageSize: 10,
//       pageNum: currentPage,
//       productLineId: productLineId.value,
//       caseType: 0,
//       title: nameFilter || '',
//       creator: createrFilter || '',
//       channel: 1,
//       requirementId: iterationFilter || '',
//       beginTime: choiseDate.length > 0 ? `${choiseDate[0]} 00:00:00` : '',
//       endTime: choiseDate.length > 0 ? `${choiseDate[1]}  23:59:59` : '',
//       bizId: caseIds.value? caseIds.value : 'root',
//       caseKeyWords: caseKeyWords || '',
//     });
//     if (res.code === 200) {
//       list.value = res.data.dataSources;
//       total.value = res.data.total;
//       current.value = currentPage;
//     } else {
//       ElMessage.error(res.msg);
//     }
//     loading.value = false;
//   } catch (error) {
//     ElMessage.error('获取测试用例列表失败');
//   }
// };


/** 查询测试用例列表 */
function getList() {
  queryParams.value.bizId = bizId.value;
  loading.value = true;
  listCase(queryParams.value).then(response => {
    caseList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    groupId: null,
    title: null,
    description: null,
    isDelete: null,
    creator: null,
    modifier: null,
    caseContent: null,
    gmtCreated: null,
    gmtModified: null,
    extra: null,
    productLineId: null,
    caseType: null,
    moduleNodeId: null,
    planId: null,
    smkCaseId: null,
    channel: null,
    bizId: null
  };
  proxy.resetForm("caseRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加测试用例";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getCase(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改测试用例";
  });
}
// 初始化数据，可能是测试用例的默认内容结构
const initData = `{"root":{"data":{"id":"bv8nxhi3c800","created":1562059643204,"text":"中心主题"},"children":[]},"template":"default","theme":"fresh-blue","version":"1.4.43","base":0}`;

/** 提交按钮 */
function submitForm() {
  proxy.$refs["caseRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        editCase(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
          refreshTreeData();
        });
      } else {
        form.value.caseContent = initData;
        
        createOrCopyCase(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;

          getList();
           
   
          //添加成功跳转
        //   let urls = `${this.props.baseUrl}/caseManager/${this.props.productId}/${res.data}/undefined/0`;
        //   router.push(urls); // 如果是新建操作，跳转到相应页面
        });
      }
    }
  });
}

//刷新
function refreshTreeData() {
  listBiz().then(response => {
    if (response.code === 200) {
      // 获取响应中的树形数据
      const newTreeData = response.data.children;
      // 更新树形数据
      updateTreeData(newTreeData);
      // 初始化展开节点
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  const id = row.id;
  //调试
  proxy.$modal.confirm('是否确认删除用例名字为"' + row.title + '"的数据项？').then(function() {
    return delCase(_ids);
  }).then(() => {
    getList();
    refreshTreeData();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/case/export', {
    ...queryParams.value
  }, `case_${new Date().getTime()}.xlsx`)
}

getList();



//创建执行任务模块
import { 
  listRecord, 
  getRecord, 
  delRecord, 
  addRecord, 
  updateRecord,
  exportRecord,
  createRecord,
  editRecord,
  deleteRecord,
  clearRecord,
  getRecordGeneralInfo 
} from "@/api/manager/record";


const TaskOpen = ref(false);
const TaskTitle = ref('');
const TaskForm = reactive({
   id: null,
  caseId: null,
  title: null,
  description: null,
  chooseContent: null,
  gmtCreated: null,
  gmtModified: null,
  expectStartTime: null,
  expectEndTime: null,
  actualStartTime: null,
  actualEndTime: null,
  owner: null
});
const TaskRules = {
  TaskTitle: [
    { required: true, message: '请输入用例名称', trigger: 'blur' }
  ]
};
const radioValue = ref('0');
const selectValue = ref([]);
const resource = ref([]);
const choiseDate = ref([]);
const TaskGrade = ['P0', 'P1', 'P2'];
const TaskCaseInfo = ref({ taglist: [] });

// 表单重置
const Taskproxy = getCurrentInstance().proxy;
function TaskReset() {
  TaskForm.id = null;
  TaskForm.caseId = null;
  TaskForm.title = null;
  TaskForm.description = null;
  TaskForm.chooseContent = null;
  TaskForm.gmtCreated = null;
  TaskForm.gmtModified = null;
  TaskForm.expectStartTime = null;
  TaskForm.expectEndTime = null;
  TaskForm.actualStartTime = null;
  TaskForm.actualEndTime = null;
  TaskForm.owner = null;
  radioValue.value = '0';
  selectValue.value = [];
  resource.value = [];
  choiseDate.value = [];
  proxy.resetForm("TaskRecordRef");
}

/** 新增按钮操作 */
const fromCaseId = ref(null);
function TaskHandleAdd(row) {
  //调试
  fromCaseId.caseId = row.id;
  console.log(TaskForm.caseId);

  TaskReset();
  TaskOpen.value = true;
  TaskTitle.value = "新增测试执行";
}

/** 修改按钮操作 */
function TaskHandleUpdate(row) {
  // console.log(row);
  TaskReset();
  const _id = row.id || ids.value;
  getRecord(_id).then(response => {
    
    TaskForm.value = response.data;
    console.log(TaskForm.value.chooseContent);
    TaskOpen.value = true;
    TaskTitle.value = "修改测试执行";
    choiseDate.value = [TaskForm.value.expectStartTime, TaskForm.value.expectEndTime];
    TaskForm.id = TaskForm.value.id;
    TaskForm.caseId = TaskForm.value.caseId;
    TaskForm.title = TaskForm.value.title;
    TaskForm.description = TaskForm.value.description;
    TaskForm.chooseContent = TaskForm.value.chooseContent;
    TaskForm.gmtCreated = TaskForm.value.gmtCreated;
    TaskForm.gmtModified = TaskForm.value.gmtModified;
    TaskForm.expectStartTime = TaskForm.value.expectStartTime;
    TaskForm.expectEndTime = TaskForm.value.expectEndTime;
    TaskForm.actualStartTime = TaskForm.value.actualStartTime;
    TaskForm.actualEndTime = TaskForm.value.actualEndTime;
    TaskForm.owner = TaskForm.value.owner;
    console.log("0-" + TaskForm.caseId);
    
    // 处理选择内容
    // console.log("1-" + TaskForm.value.chooseContent);
    const chooseContent = JSON.parse(TaskForm.value.chooseContent);
    //  console.log("2-" + chooseContent.priority);
    radioValue.value = chooseContent.priority.indexOf('0') > -1 ? '0' : '1';
    // console.log("5-" + radioValue.value);
    selectValue.value = radioValue.value == '1' ? chooseContent.priority : [];
    
    // console.log("6-" + selectValue.value);
    resource.value = TaskForm.value.chooseContent.resource;
    // console.log("3-" + TaskForm.value.expectStartTime);
    // console.log("4-" + TaskForm.value.expectEndTime);
    
  });
}

/** 提交按钮 */
function TaskSubmitForm() {
 proxy.$refs["TaskRecordRef"].validate(valid => {
    if (valid) {
      TaskForm.chooseContent = JSON.stringify({
        priority: radioValue.value === '0' ? ['0'] : selectValue.value,
        resource: resource.value
      });
      //choiseDate.value[0] 转换为long类型
       // 将日期字符串转换为时间戳（long类型）
      TaskForm.expectStartTime = Date.parse(choiseDate.value[0]);
      TaskForm.expectEndTime = Date.parse(choiseDate.value[1]);
       console.log("123"+TaskForm.caseId);
      if (TaskForm.id != null) {
        editRecord(TaskForm).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          TaskOpen.value = false;
          getList();
          getRecordList(TaskForm.caseId);
        });
      } else {
      TaskForm.caseId = fromCaseId.caseId;
        createRecord(TaskForm).then(response => {
         proxy.$modal.msgSuccess("新增成功");
          TaskOpen.value = false;
          getList();
          getRecordList(TaskForm.caseId);
        });
      }
    }
  });
}

function TaskCancel() {
  TaskOpen.value = false;
}

function TaskGetList() {
  // 实现获取列表数据的逻辑
}




/**
 * 测试执行任务列表
 */
const recordList = ref([]);
// 新增状态
const recordLoading = ref(false)
const expandedRow = ref(null) // 当前展开的行

/** 查询用例执行记录列表 */
function getRecordList(caseId) {
  // 如果点击的是已展开的行，则关闭
    // if (expandedRow.value === row.id) {
    //   expandedRow.value = null
    //   return
    // }
  loading.value = true;
  queryParams.value.caseId = caseId;
  listRecord(queryParams.value).then(response => {
    recordList.value[caseId] = response.rows // 以caseId为键存储数据
    total.value = response.total;
    loading.value = false;
     
    // expandedRow.value = caseId // 记录当前展开的行
  });
  

  
}

function handleExpand(row) {
  
    getRecordList(row.id)
  
}


/** 查询所有计划 */
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/manager/plan";
//新增api/page
import { loadAllParams } from "@/api/page/page";


const planList = ref([]);
/** 查询测试计划列表 */
function getPlanList() {
  loading.value = true;
  listPlan(loadAllParams).then(response => {
    planList.value = response.rows;
    total.value = response.total;
    loading.value = false;
    console.log(planList.value);
  });
}

  getPlanList();


// 根据planId从planList中获取对应的planName
function getPlanNameByPlanId(planId) {
  console.log("6846848646"+planId);

  const plan = planList.value.find(p => p.id == planId);
  console.log(plan);
  return plan? plan.planName : '无关联计划';
}

import { getToken } from "@/utils/auth"

//查看用例
function locateCaseUrl(row) {
  const currentToken = getToken();
  // 拼接带参数的URL
  const targetUrl = `http://192.168.146.1:8088/#/?caseId=${row.id}&token=${encodeURIComponent(currentToken)}`;
  // 新窗口打开
  window.open(targetUrl);
}

//执行计划
function locateExectUrl(row) {
  const currentToken = getToken();
  // 拼接带参数的URL
  const targetUrl = `http://192.168.146.1:8088/#/?caseId=${row.caseId}&recordId=${row.id}&token=${encodeURIComponent(currentToken)}`;
  // 新窗口打开
  window.open(targetUrl);
}

</script>

<style scoped>
.file-tree-container {
  padding: 16px;
  background: #fff;
  border-right: 1px solid #e8ecef;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  height: 100%;
  overflow-y: auto;
  border-radius: 8px 0 0 8px;

  &::-webkit-scrollbar {
    width: 6px;
    background: #f8f9fa;
  }
  &::-webkit-scrollbar-thumb {
    background: #d8dce0;
    border-radius: 4px;
  }

  .el-tree {
    background: transparent;
    flex: 1;
    :deep(.el-tree-node) {
      margin: 4px 0;
      transition: all 0.2s;
      
      &:hover {
        background: #f5f7fa;
        .el-tree-node__content {
          background: transparent;
        }
      }
      
      &.is-current {
        > .el-tree-node__content {
          position: relative;
          &::after {
            content: "";
            position: absolute;
            left: -8px;
            top: 2px;
            bottom: 2px;
            width: 3px;
            background: #409eff;
            border-radius: 2px;
          }
        }
      }
    }
  }

  .el-dropdown {
    margin-left: 8px;
    visibility: hidden;
  }
  
  :deep(.el-tree-node__content:hover) {
    .el-dropdown {
      visibility: visible;
    }
  }

  .input-error {
    :deep(.el-input__wrapper) {
      box-shadow: 0 0 0 1px #f56c6c;
    }
  }
  
  .error-message {
    color: #f56c6c;
    font-size: 12px;
    margin-top: 4px;
  }
}

.el-dropdown + .el-dropdown {
  display: none;
}

.el-tree-node__content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-button--link {
  padding: 0 5px;
}

/* 输入框样式调整 */
.el-input__inner {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  transition: border-color 0.3s;
}

.el-input__inner:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 错误状态样式 */
.input-error .el-input__inner {
  border-color: #f56c6c;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}

.split-layout {
  display: flex;
  height: 100vh;
}

.left-panel {
  width: 30%;
  min-width: 100px;
  height: 100%;
  overflow-y: auto;
  padding: 16px;
  box-sizing: border-box;
}

.resizer {
  width: 5px;
  height: 100%;
  background-color: #ccc;
  cursor: col-resize;
}

.right-panel {
  flex: 1;
  height: 100%;
  overflow-y: auto;
  padding: 16px;
  box-sizing: border-box;
}

.folder-name {
  display: inline-block;
  max-width: 150px;  
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: bottom;
}

/* 调整操作按钮布局 */
.el-tree-node__content {
  :deep(.el-tree-node__label) {
    flex: 1;
    min-width: 0;  
  }
}

:deep(.el-tooltip__trigger span) {
  display: inline-block;
  max-width: 100%;
}

.case-count {
  margin-left: 8px;
  color: #909399;
  font-size: 12px;
  flex-shrink: 0; /* 防止数字被挤压 */
}

.action-btn {
  margin-right: 2px;
  
  :deep(.el-icon) {
    font-size: 18px;
  }
  
  &:hover {
    transform: scale(1.1);
    transition: transform 0.2s;
  }
}
</style>