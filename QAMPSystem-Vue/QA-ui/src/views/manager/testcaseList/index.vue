<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <!-- <el-form-item label="执行的用例id" prop="caseId">
        <el-input
          v-model="queryParams.caseId"
          placeholder="请输入执行的用例id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用例名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入用例名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行环境： 0、测试环境 1、预发环境 2.线上环境 3.冒烟qa 4.冒烟rd" prop="env">
        <el-input
          v-model="queryParams.env"
          placeholder="请输入执行环境： 0、测试环境 1、预发环境 2.线上环境 3.冒烟qa 4.冒烟rd"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用例状态 0-正常 1-删除" prop="isDelete">
        <el-input
          v-model="queryParams.isDelete"
          placeholder="请输入用例状态 0-正常 1-删除"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行个数" prop="passCount">
        <el-input
          v-model="queryParams.passCount"
          placeholder="请输入执行个数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="需执行总个数" prop="totalCount">
        <el-input
          v-model="queryParams.totalCount"
          placeholder="请输入需执行总个数"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="成功个数" prop="successCount">
        <el-input
          v-model="queryParams.successCount"
          placeholder="请输入成功个数"
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
      <el-form-item label="用例修改人" prop="modifier">
        <el-input
          v-model="queryParams.modifier"
          placeholder="请输入用例修改人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行人" prop="executors">
        <el-input
          v-model="queryParams.executors"
          placeholder="请输入执行人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="记录创建时间" prop="gmtCreated">
        <el-date-picker clearable
          v-model="queryParams.gmtCreated"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择记录创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="记录修改时间" prop="gmtModified">
        <el-date-picker clearable
          v-model="queryParams.gmtModified"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择记录修改时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预计开始时间" prop="expectStartTime">
        <el-date-picker clearable
          v-model="queryParams.expectStartTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择预计开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预计结束时间" prop="expectEndTime">
        <el-date-picker clearable
          v-model="queryParams.expectEndTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择预计结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际开始时间" prop="actualStartTime">
        <el-date-picker clearable
          v-model="queryParams.actualStartTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择实际开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际结束时间" prop="actualEndTime">
        <el-date-picker clearable
          v-model="queryParams.actualEndTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择实际结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="负责人" prop="owner">
        <el-input
          v-model="queryParams.owner"
          placeholder="请输入负责人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['manager:record:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:record:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['manager:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column label="任务ID" align="center" prop="id" />
      <!-- <el-table-column label="执行的用例id" align="center" prop="caseId" /> -->
      <el-table-column prop="title"  align="center" label="任务名称" width="120">
                  <template #default="scope">
                    <el-link type="primary" @click="handleTaskClick(row)">
                      {{ scope.row.title }}
                    </el-link>
                  </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="owner" />
      <el-table-column label="执行人" align="center" prop="executors" />
      
      <el-table-column label="通过率" width="100" align="center">
        <template #default="scope">
          {{ scope.row.totalNum > 0 ? 
              Math.round((scope.row.successNum / scope.row.totalNum) * 100) + '%' : 
              'N/A' }}
        </template>
      </el-table-column>
      <el-table-column label="已测用例集" width="150" align="center">
        <template #default="{row}">
          {{ row.executeNum || 0 }} / {{ row.totalNum || 0 }}
        </template>
      </el-table-column>
      <el-table-column prop="expectTime" label="期望时间" width="200">
        <template #default="scope">
          {{ parseTime(scope.row.expectStartTime, '{y}-{m}-{d}') }} 至 
          {{ parseTime(scope.row.expectEndTime, '{y}-{m}-{d}') }}
        </template>
      </el-table-column>
      
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:record:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:record:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
   
    <!-- 添加或修改用例执行记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="recordRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="执行的用例id" prop="caseId">
          <el-input v-model="form.caseId" placeholder="请输入执行的用例id" />
        </el-form-item>
        <el-form-item label="用例名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入用例名称" />
        </el-form-item>
       
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="圈选用例内容">
          <editor v-model="form.chooseContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="记录创建时间" prop="gmtCreated">
          <el-date-picker clearable
            v-model="form.gmtCreated"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择记录创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="记录修改时间" prop="gmtModified">
          <el-date-picker clearable
            v-model="form.gmtModified"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择记录修改时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预计开始时间" prop="expectStartTime">
          <el-date-picker clearable
            v-model="form.expectStartTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择预计开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预计结束时间" prop="expectEndTime">
          <el-date-picker clearable
            v-model="form.expectEndTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择预计结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际开始时间" prop="actualStartTime">
          <el-date-picker clearable
            v-model="form.actualStartTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择实际开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实际结束时间" prop="actualEndTime">
          <el-date-picker clearable
            v-model="form.actualEndTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择实际结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="负责人" prop="owner">
          <el-input v-model="form.owner" placeholder="请输入负责人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Record">
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
const { proxy } = getCurrentInstance();

const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    caseId: null,
    title: null,
    env: null,
    caseContent: null,
    isDelete: null,
    passCount: null,
    totalCount: null,
    successCount: null,
    creator: null,
    modifier: null,
    executors: null,
    description: null,
    chooseContent: null,
    gmtCreated: null,
    gmtModified: null,
    expectStartTime: null,
    expectEndTime: null,
    actualStartTime: null,
    actualEndTime: null,
    owner: null
  },
  rules: {
    caseId: [
      { required: true, message: "执行的用例id不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);
const recordList = ref([]);

/** 查询用例执行记录列表 */
function getRecordList() {
  loading.value = true;
  queryParams.value.caseId = 5;
  listRecord(queryParams.value).then(response => {
    recordList.value = response.rows;
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
    caseId: null,
    title: null,
    env: null,
    caseContent: null,
    isDelete: null,
    passCount: null,
    totalCount: null,
    successCount: null,
    creator: null,
    modifier: null,
    executors: null,
    description: null,
    chooseContent: null,
    gmtCreated: null,
    gmtModified: null,
    expectStartTime: null,
    expectEndTime: null,
    actualStartTime: null,
    actualEndTime: null,
    owner: null
  };
  proxy.resetForm("recordRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getRecordList();
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
  title.value = "添加用例执行记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getRecord(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改用例执行记录";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["recordRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getRecordList();
        });
      } else {
        addRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getRecordList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除用例执行记录编号为"' + _ids + '"的数据项？').then(function() {
    return delRecord(_ids);
  }).then(() => {
    getRecordList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/record/export', {
    ...queryParams.value
  }, `record_${new Date().getTime()}.xlsx`)
}

getRecordList();
</script>
