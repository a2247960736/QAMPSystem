<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="业务ID" prop="reqId">
        <el-input
          v-model="queryParams.reqId"
          placeholder="请输入业务ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属项目" prop="projectId">
        <el-select 
          v-model="queryParams.projectId" 
          placeholder="请选择项目" 
          clearable
        >
          <el-option
            v-for="project in projectOptions" 
            :key="project.value"
            :label="project.label"  
            :value="project.value"   
          />
        </el-select>
      </el-form-item>
      <el-form-item label="需求标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入需求标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="请选择优先级" clearable>
          <el-option
            v-for="dict in priority"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="需求状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择需求状态" clearable>
          <el-option
            v-for="dict in req_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['manager:requirement:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:requirement:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:requirement:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['manager:requirement:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="requirementList" @selection-change="handleSelectionChange">
       <el-table-column type="selection" width="55" align="center" />
       <el-table-column label="序号" align="center" width="80">
    <template #default="scope">
      {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
    </template>
  </el-table-column>
      <el-table-column label="业务ID" align="center" prop="reqId" />
     <el-table-column label="所属项目" align="center" prop="projectId">
  <template #default="scope">
    {{ scope.row.proProject?.projectName || '未知项目' }}  <!-- 直接显示嵌套对象中的项目名称 -->
  </template>
</el-table-column>
      <el-table-column label="需求标题" align="center" prop="title" />
      <el-table-column 
  label="优先级" 
  align="center" 
  prop="priority"
  width="120"
  sortable
>
  <template #default="scope">
    <el-tag 
      :type="{'P0':'danger', 'P1':'warning', 'P2':'success'}[scope.row.priority]"
      effect="dark"
    >
      {{ priority.find(v => v.value === scope.row.priority)?.label || 'N/A' }}
    </el-tag>
  </template>
</el-table-column>

<el-table-column 
  label="需求状态" 
  align="center" 
  prop="status"
  width="150"
  sortable
>
  <template #default="scope">
    <el-tag
      :type="{
        'NEW':'info',
        'IN_PROGRESS':'primary',
        'BLOCKED':'danger',
        'DONE':'success'
      }[scope.row.status]"
      :effect="scope.row.status === 'IN_PROGRESS' ? 'light' : 'dark'"
    >
      {{ req_status.find(v => v.value === scope.row.status)?.label || 'N/A' }}
    </el-tag>
  </template>
</el-table-column>
      <el-table-column 
  label="操作" 
  align="center" 
  class-name="small-padding fixed-width"
  width="300"
>
  <template #default="scope">
    <el-space :size="25">
      <el-button 
        link 
        type="success" 
        icon="DocumentAdd" 
        @click="handleCreateTestPlan(scope.row)"
        v-hasPermi="['manager:requirement:createPlan']">创建测试计划</el-button>
      <el-button 
        link 
        type="primary" 
        icon="Edit" 
        @click="handleUpdate(scope.row)"
        v-hasPermi="['manager:requirement:edit']">修改</el-button>
      <el-button 
        link 
        type="danger" 
        icon="Delete" 
        @click="handleDelete(scope.row)"
        v-hasPermi="['manager:requirement:remove']">删除</el-button>
    </el-space>
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

    <!-- 添加或修改需求管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="requirementRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="业务标识" prop="reqId">
          <el-input v-model="form.reqId" placeholder="请输入业务标识" />
        </el-form-item>
        <el-form-item label="所属项目" prop="projectId" >
            <el-select v-model="form.projectId" placeholder="请选择项目">
  <el-option
    v-for="project in projectOptions"
    :key="project.value"
    :label="project.label"
    :value="project.value"
  ></el-option>
</el-select>
</el-form-item>
        <el-form-item label="需求标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入需求标题" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="form.priority" placeholder="请选择">
            <el-option
              v-for="dict in priority"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="需求状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option
              v-for="dict in req_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
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

<script setup name="Requirement">
import { listRequirement, getRequirement, delRequirement, addRequirement, updateRequirement } from "@/api/manager/requirement";
// 新增项目API引用
import { listProject } from "@/api/manager/project";
//新增api/page
import { loadAllParams } from "@/api/page/page";
const { proxy } = getCurrentInstance();
const { priority, sys_dept, req_status } = proxy.useDict('priority', 'sys_dept', 'req_status');

const requirementList = ref([]);
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
    reqId: null,
    projectId: null,
    title: null,
    priority: null,
    status: null,
  },
  rules: {
    projectId: [
      { required: true, message: "所属项目技术ID不能为空", trigger: "change" }
    ],
    title: [
      { required: true, message: "需求标题不能为空", trigger: "blur" }
    ],
    priority: [
      { required: true, message: "优先级不能为空", trigger: "change" }
    ],
    status: [
      { required: true, message: "需求状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询需求管理列表 */
function getList() {
  loading.value = true;
  listRequirement(queryParams.value).then(response => {
    requirementList.value = response.rows;
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
    reqId: null,
    projectId: null,
    title: null,
    priority: null,
    status: null,
    createTime: null
  };
  proxy.resetForm("requirementRef");
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
  title.value = "添加需求管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getRequirement(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改需求管理";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["requirementRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateRequirement(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addRequirement(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除需求管理编号为"' + _ids + '"的数据项？').then(function() {
    return delRequirement(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/requirement/export', {
    ...queryParams.value
  }, `requirement_${new Date().getTime()}.xlsx`)
}

// 在 reactive 数据对象中添加项目列表响应式变量
const projectOptions = ref([]);
/** 查询项目管理列表 */
function getProjectList() {
  loading.value = true;
  listProject(loadAllParams).then(response => {
   projectOptions.value = response.rows.map(item => ({
      value: item.id,
      label: item.projectName
    }));
     // 新增路由参数匹配逻辑
    if (projectId.value) {
      const matchedProject = projectOptions.value.find(
        project => project.value == projectId.value // 使用宽松相等匹配
      );
      if (matchedProject) {
        queryParams.value.projectId = matchedProject.value; // 确保值同步
        projectName.value = matchedProject.label; // 更新项目名称显示
      }
    }
    
    loading.value = false;
  })
}

getProjectList();

getList();
</script>
