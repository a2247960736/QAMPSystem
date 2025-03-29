<template>
  <div class="file-tree-container">
    <el-input
      v-model="searchValue"
      placeholder="搜索类别"
      @input="handleSearch"
      style="margin-bottom: 16px"
    />
    <el-tree
      :data="treeData"
      :props="treeProps"
      :expand-on-click-node="false"
      :draggable="true"
      :default-expand-all="true"
      :default-expanded-keys="expandedKeys"
      :auto-expand-parent="autoExpandParent"
      @node-click="handleNodeClick"
      @node-expand="handleNodeExpand"
      @drop="handleDrop"
    >
      <template #default="{ node, data }">
        <div v-if="data.isEdit">
          <el-input
            v-model="data.text"
            ref="inputRef"
            @focus="selectInputText"
            @blur="handleInputBlur(data)"
            @keyup.enter="handleInputBlur(data)"
          />
        </div>
        <!-- 修改树节点模板部分 -->
        <div v-else>
          <span v-html="highlightSearch(data.text)"></span>
          <span style="font-family: 'PingFangSC-Regular'; margin-left: 4px">
            ({{ data.caseIds ? data.caseIds.length : 0 }})
          </span>
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
                <el-dropdown-item 
                  v-if="data.id !== 'root'" 
                  command="addSibling"
                >添加同级文件夹</el-dropdown-item>
                <el-dropdown-item 
                  v-if="data.id !== '-1'" 
                  command="addChild"
                >添加子文件夹</el-dropdown-item>
                <el-dropdown-item 
                  v-if="data.id !== '-1'" 
                  command="rename"
                >重命名</el-dropdown-item>
                <el-dropdown-item 
                  v-if="data.id !== '-1' && data.id !== 'root'" 
                  command="delete"
                >删除</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        
        <el-dropdown @command="handleMenuCommand(data, $event)">
          <i class="el-icon-more" style="margin-left: 8px"></i>
          <!-- 保持原有下拉菜单内容不变 -->
        </el-dropdown>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, nextTick } from 'vue';
import { listBiz, getBiz, delBiz, addBiz, updateBiz, renameDir, deleteDir, getDirCardTree, moveDir } from "@/api/manager/biz";
import { ElMessage, ElMessageBox } from 'element-plus';
import { MoreFilled } from '@element-plus/icons-vue'

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

const searchValue = ref('');
const expandedKeys = ref(['root']);
const autoExpandParent = ref(true);
const originalTreeData = ref([]);
const dataList = ref([]);
const treeData = ref([]);
const inputRef = ref(null);

const treeProps = {
  children: 'children',
  label: 'text'
};

onMounted(async () => {
  try {
    const response = await listBiz();
    if (response.code === 200) {
      const newTreeData = response.data.children;
      updateTreeData(newTreeData);
      initTreeExpandedKeys(false);
    } else {
      ElMessage.error(response.msg);
    }
  } catch (error) {
    ElMessage.error('请求出错，请稍后重试');
  }
});

watch(() => props.treeData, (newTreeData) => {
  updateTreeData(newTreeData);
  initTreeExpandedKeys(true);
});

const updateTreeData = (newTreeData) => {
  treeData.value = JSON.parse(JSON.stringify(newTreeData));
};

const initTreeExpandedKeys = (expandedAll) => {
  dataList.value = [];
  generateList(treeData.value);
  expandedKeys.value = expandedAll
    ? dataList.value.map(item => item.id)
    : expandedKeys.value;
};

const generateList = (data, excludeItem = {}) => {
  for (let i = 0; i < data.length; i++) {
    const node = data[i];
    const id = node.id;
    if (excludeItem.id!== node.id) {
      dataList.value.push({ id, text: node.text });
    }
    if (node.children) {
      generateList(node.children);
    }
  }
};

const handleSearch = () => {
  const newExpandedKeys = dataList.value
    .map(item => {
      if (item.text.indexOf(searchValue.value) > -1) {
        return getParentKey(item.text, treeData.value);
      }
      return null;
    })
    .filter((item, i, self) => item && self.indexOf(item) === i);
  expandedKeys.value = newExpandedKeys;
  autoExpandParent.value = true;
};

const getParentKey = (title, tree) => {
  let parentKey;
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i];
    if (node.children) {
      if (node.children.some(item => item.text === title)) {
        parentKey = node.id;
      } else if (getParentKey(title, node.children)) {
        parentKey = getParentKey(title, node.children);
      }
    }
  }
  return parentKey;
};

// 修改节点点击处理函数
const handleNodeClick = (data) => {


  if (props.getCaseList && typeof props.getCaseList === 'function') {
    props.getCaseList([data.id])
  } else {
    console.error('getCaseList prop is not a valid function')
  }
}

const handleNodeExpand = (expandedKeys) => {
  expandedKeys.value = expandedKeys;
  autoExpandParent.value = false;
};

const handleMenuCommand = (data, command) => {
  switch(command) {
    case 'addSibling':
      addSibling(data)
      break
    case 'addChild':
      addChild(data)
      break
    case 'rename':
      rename(data)
      break
    case 'delete':
      isDelete(data)
      break
  }
}

const addNode = (key, data) => {
   return data.map(item => {
    const node = { ...item };
    if (key === item.id) {
      return {
        ...item,
        children: [
          ...(item.children || []),
          {
            parentId: item.id,
            id: Date.now(), // 使用时间戳避免ID重复
            text: '',
            children: [],
            isEdit: true
          }
        ]
      };
    }
    if (node.children) {
      node.children = addNode(key, node.children);
    }
    return node;
  });
};

const addSibling = (item) => {
  treeData.value = addNode(item.parentId, treeData.value);
};

const addChild = (item) => {
  treeData.value = addNode(item.id, treeData.value);
 // 保持父节点展开状态
  nextTick(() => {
    // 添加父节点ID和当前节点ID到展开列表
    expandedKeys.value = Array.from(new Set([
      ...expandedKeys.value,
      item.parentId,
      item.id
    ]));
    autoExpandParent.value = true;
  });
};

const rename = (item) => {
  editNode(item.id, treeData.value);
};

const editNode = (key, data) => {
  data.forEach(item => {
    // 当 key 为 null 时清除所有编辑状态
    if (key === null) {
      item.isEdit = false;
      item.rename = false;
    } else {
      if (item.id === key) {
        item.isEdit = true;
        item.rename = true;
      } else {
        item.isEdit = false;
        item.rename = false;
      }
    }
    if (item.children) {
      editNode(key, item.children);
    }
  });
};

const isDelete = (item) => {
  ElMessageBox.confirm(
    `确认删除文件夹吗？删除 ${item.text} 文件夹时，同时会删除包含的 ${item.caseIds ? item.caseIds.length : 0} 个用例集`,
    '提示',
    {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    deleteFolder(item);
  }).catch(() => {});
};

const deleteFolder = (item) => {
  deleteDir({
    parentId: item.parentId,
    productLineId: props.productLineId,
    delId: item.id,
    channel: 1
  }).then((res) => {
     if (res.code === 200) {
      ElMessage.success('删除成功');
      props.getTreeList(true); // 强制刷新树数据
      exitEditMode();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const handleInputBlur = (item) => {
  if (item.text === '') {
    treeData.value = originalTreeData.value;
  } else {
    if (item.rename) {
      renameInput(item);
    } else {
      onblurInput(item);
    }
  }
   // 立即退出编辑模式
  editNode(null, treeData.value); // 新增这行
};

const onblurInput = (item) => {
  addBiz({
    parentId: item.parentId,
    productLineId: props.productLineId,
    text: item.text,
    channel: 1
  }).then((res) => {
     if (res.code === 200) {
      ElMessage.success('添加成功');
      props.getTreeList(); // 重新获取数据
      exitEditMode(); // 退出编辑模式
    }else {
      ElMessage.error(res.msg);
    }
  });
};

const renameInput = (item) => {
  if (item.text === '') {
    return ElMessage.error('重命名文件夹名不能为空');
  }
  renameDir({
    id: item.id,
    productLineId: props.productLineId,
    text: item.text,
    channel: 1
  }).then((res) => {
  if (res.code === 200) {
      ElMessage.success('重命名成功');
      props.getTreeList().then(() => {
        // 在数据更新后执行
        exitEditMode();
        // 保持父节点展开
        expandedKeys.value = Array.from(new Set([...expandedKeys.value, item.parentId]));
        autoExpandParent.value = true;
      });
    }else {
      editNode(null, treeData.value);
      ElMessage.error(res.msg);
    }
  });
};

// 新增退出编辑模式方法
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
    if (inputRef.value) {
      inputRef.value.$el.querySelector('input').select();
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
</script>

<style scoped>
.file-tree-container {
  padding: 16px;
}

.el-tree-node__content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-button--link {
  padding: 0 5px;
}
</style>