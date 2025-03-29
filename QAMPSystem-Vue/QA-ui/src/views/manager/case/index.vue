<template>
  <div class="all-content">
    <!-- 文件树组件 -->
    <!-- 这里可根据实际情况补充文件树组件的具体实现 -->
    <div>文件树组件待实现</div>
    <div class="min-hig-content">
      <div class="site-drawer-render-in-current-wrapper">
        <el-row class="m-b-10">
          <el-col :span="18">
            <div style="margin: 10px">
              快速筛选：<a>全部({{ total }})</a>
            </div>
          </el-col>
          <el-col :xs="6" class="text-right">
            <el-button @click="filterHandler">
              <i class="el-icon-filter"></i> 筛选
            </el-button>
            <el-button type="primary" @click="handleTask('add')">
              <i class="el-icon-plus"></i> 新建用例集
            </el-button>
          </el-col>
        </el-row>
        <hr style="border: 0; background-color: #e8e8e8; height: 1px" />
        <!-- 筛选组件 -->
        <!-- 这里可根据实际情况补充筛选组件的具体实现 -->
        <div v-if="showFilterBox">筛选组件待实现</div>
        <!-- 测试用例列表组件 -->
        <div>
          <el-table :data="list" stripe>
            <el-table-column prop="title" label="用例集名称"></el-table-column>
            <el-table-column prop="requirementId" label="关联需求"></el-table-column>
            <el-table-column prop="bizId" label="用例集分类"></el-table-column>
            <el-table-column prop="description" label="描述"></el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="mini" @click="handleTask('edit', scope.row)">编辑</el-button>
                <el-button size="mini" type="danger" @click="handleTask('delete', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="current"
            :page-sizes="[10, 20, 30]"
            :page-size="10"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
          >
          </el-pagination>
        </div>
        <!-- 筛选组件 -->
        <!-- 这里可根据实际情况补充筛选组件的具体实现 -->
        <div v-if="filterVisble">筛选组件待实现</div>
        <!-- 测试用例模态框组件 -->
        <el-dialog :visible.sync="visible" title="测试用例模态框" width="600px">
          <el-form :model="formData" ref="formRef" label-width="100px">
            <el-form-item label="用例集名称" :rules="{ required: true, message: '请填写用例集名称', trigger: 'blur' }">
              <el-input v-model="formData.case"></el-input>
            </el-form-item>
            <el-form-item label="关联需求">
              <el-input v-model="formData.requirementId"></el-input>
            </el-form-item>
            <el-form-item label="用例集分类" :rules="{ required: true, message: '请选择用例集分类', trigger: 'change' }">
              <el-tree-select
                v-model="formData.bizId"
                :data="cardTree"
                :props="{ label: 'text', value: 'id' }"
                multiple
                allow-clear
                placeholder="请选择用例"
              ></el-tree-select>
            </el-form-item>
            <el-form-item label="描述">
              <el-input type="textarea" v-model="formData.description" :autosize="{ minRows: 4 }" maxlength="1024"></el-input>
            </el-form-item>
            <el-form-item v-if="operate === 'add'">
              <span>导入用例文件:</span>
              <el-upload
                class="upload-demo"
                :action="uploadUrl"
                :before-upload="beforeUpload"
                :on-remove="onRemove"
                :file-list="fileList"
                accept=".xmind,.xls,.xlsx"
              >
                <template #trigger>
                  <el-button size="small" type="primary">选择文件</el-button>
                </template>
                <template #tip>
                  <div class="el-upload__tip">
                    支持.xmind和excel文件，excel请按照
                    <a href="@/views/manager/case/testcase-template.xlsx" download="testcase-template.xlsx">模板</a>
                    填写...
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="onClose">取消</el-button>
              <el-button type="primary" @click="handleOk">确认</el-button>
            </span>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import {
  listCase,
  getCase,
  delCase,
  addCase,
  updateCase,
  getCaseGeneralInfo,
  getCountByCondition,
  listCreators,
  getCaseDetail,
  deleteCaseById,
  editCase,
  createOrCopyCase,
} from '@/api/manager/case';
import { getDirCardTree } from '@/api/manager/biz';

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
const treeData = ref([]);
const levelId = ref('');
const levelText = ref('');
const searchValue = ref('');
const autoExpandParent = ref(true);
const dataList = ref([]);
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

// 获取文件树列表
const getTreeList = async (isManual = false) => {
  try {
    const res = await getDirCardTree({
      productLineId: productLineId.value,
      channel: 1,
    });
    if (res.code === 200) {
      treeData.value = res.data.children;
      caseIds.value = treeSelect.value.length > 0 ? treeSelect.value.toString() : caseIds.value;
      if (!isManual) {
        getCaseList(1, '', '', '', []);
      }
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    ElMessage.error('获取文件树列表失败');
  }
};

// 获取测试用例列表
const getCaseList = async (
  currentPage,
  nameFilter,
  createrFilter,
  iterationFilter,
  choiseDate = [],
  caseKeyWords = ''
) => {
  try {
    const res = await listCase({
      pageSize: 10,
      pageNum: currentPage,
      productLineId: productLineId.value,
      caseType: 0,
      title: nameFilter || '',
      creator: createrFilter || '',
      channel: 1,
      requirementId: iterationFilter || '',
      beginTime: choiseDate.length > 0 ? `${choiseDate[0]} 00:00:00` : '',
      endTime: choiseDate.length > 0 ? `${choiseDate[1]}  23:59:59` : '',
      bizId: caseIds.value? caseIds.value : 'root',
      caseKeyWords: caseKeyWords || '',
    });
    if (res.code === 200) {
      list.value = res.data.dataSources;
      total.value = res.data.total;
      current.value = currentPage;
    } else {
      ElMessage.error(res.msg);
    }
    loading.value = false;
  } catch (error) {
    ElMessage.error('获取测试用例列表失败');
  }
};

// 初始化测试用例模态框信息
const initCaseModalInfo = () => {
  let { requirementLs } = options.value;
  let requirement = null;
  options.value = {
    requirement,
    requirementLs,
  };
};

// 获取相关人员列表
const getProductMumber = async () => {
  try {
    const res = await listCreators({ productLineId: productLineId.value, caseType: 0 });
    if (res.code === 200) {
      productMember.value = res.data;
    }
  } catch (error) {
    ElMessage.error('获取相关人员列表失败');
  }
};

// 处理任务
const handleTask = (val, record = null) => {
  visible.value = true;
  title.value = val;
  currCase.value = record;
  operate.value = val;
  if (record) {
    formData.value = {
      case: record.title,
      requirementId: record.requirementId,
      bizId: record.bizId,
      description: record.description,
    };
  } else {
    formData.value = {
      case: '',
      requirementId: '',
      bizId: [],
      description: '',
    };
  }
  formRef.value.resetFields();
};

// 点击显示筛选框按钮
const onShowFilterBoxClick = () => {
  showFilterBox.value =!showFilterBox.value;
  // 清空筛选条件
  // 这里可根据实际情况补充清空筛选条件的逻辑
};

// 关闭模态框
const onClose = () => {
  visible.value = false;
};

// 打开筛选框
const filterHandler = () => {
  filterStatus.value = 'filter-show';
  filterVisble.value = true;
};

// 关闭筛选框
const closeFilter = () => {
  filterStatus.value = 'filter-hide';
  filterVisble.value = false;
};

// 点击模态框确认按钮
const handleOk = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      if (operate.value!== 'edit') {
        await saveEditerData(formData.value);
      } else {
        await renameOk(formData.value);
      }
    }
  });
};

// 确认新建测试用例集
const saveEditerData = async (values) => {
  let requirementId = values.requirementId;
  let params = {
    productLineId: Number(productLineId.value),
    creator: 'username', // 这里需要根据实际情况获取用户名
    caseType: 0,
    caseContent: `{"root":{"data":{"id":"bv8nxhi3c800","created":1562059643204,"text":"中心主题"},"children":[]},"template":"default","theme":"fresh-blue","version":"1.4.43","base":0}`,
    title: values.case,
    channel: 1,
    bizId: values.bizId? values.bizId.join(',') : '-1',
    id: operate.value!== 'add'? currCase.value.id : '',
    requirementId,
    description: values.description,
  };

  let url = '';
  if (fileList.value.length > 0) {
    const file = fileList.value[0];
    if (/(?:xmind)$/i.test(file.name)) {
      url = `${process.env.VUE_APP_BASE_API}/file/import`;
    } else if (/(?:xls|xlsx)$/i.test(file.name)) {
      url = `${process.env.VUE_APP_BASE_API}/file/importExcel`;
    }

    params = new FormData();
    params.append('file', file);
    params.append('creator', 'username'); // 这里需要根据实际情况获取用户名
    params.append('title', values.case);
    params.append('productLineId', Number(productLineId.value));
    params.append('requirementId', requirementId);
    params.append('description', values.description);
    params.append('channel', 1);
    params.append('bizId', values.bizId? values.bizId.join(',') : '-1');
  } else {
    url = `${process.env.VUE_APP_BASE_API}/case/create`;
  }

  try {
    const res = await createOrCopyCase({ url, method: 'POST', body: params });
    if (res.code === 200) {
      ElMessage.success(operate.value === 'add'? '新建测试用例集成功' : '复制测试用例集成功');
      if (operate.value === 'add') {
        // 跳转到相应页面
        // 这里需要根据实际情况实现跳转逻辑
      }
      onClose();
      getCaseList(current.value, '', '', '', []);
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    ElMessage.error('保存测试用例集失败');
  }
};

// 确认重命名测试用例集
const renameOk = async (values) => {
  let requirementId = values.requirementId;
  let params = {
    title: values.case,
    id: currCase.value.id,
    requirementId,
    caseType: 0,
    description: values.description,
    modifier: 'username', // 这里需要根据实际情况获取用户名
    bizId: values.bizId? values.bizId.join(',') : '-1',
    channel: 1,
  };

  try {
    const res = await editCase(params);
    if (res.code === 200) {
      ElMessage.success('更新成功');
      getCaseList(current.value, '', '', '', []);
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    ElMessage.error('重命名测试用例集失败');
  }
};

// 文件移除
const onRemove = (file) => {
  fileList.value = [];
};

// 文件上传前
const beforeUpload = (file) => {
  const isLt2M = file.size / 1024 / 1024 <= 100;
  if (!isLt2M) {
    ElMessage.error('用例集文件大小不能超过100M');
    return false;
  }
  fileList.value = [file];
  return false;
};

// 分页相关
const handleSizeChange = (newSize) => {
  getCaseList(1, '', '', '', []);
};

const handleCurrentChange = (newPage) => {
  getCaseList(newPage, '', '', '', []);
};

// 组件挂载完成
onMounted(async () => {
  productLineId.value = '123'; // 这里需要根据实际情况获取产品线 ID
  await getProductMumber();
  await getTreeList();
});
</script>

<style scoped>
/* 这里可以添加样式 */
</style>