<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="业务标识" prop="projectId">
        <el-input
          v-model="queryParams.projectId"
          placeholder="请输入业务标识"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属部门" prop="deptId">
        <el-select v-model="queryParams.deptId" placeholder="请选择所属部门" clearable>
          <el-option
            v-for="dict in sys_dept"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['manager:project:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:project:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:project:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['manager:project:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="80">
    <template #default="scope">
      {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
    </template>
  </el-table-column>
      <el-table-column label="业务ID" align="center" prop="projectId" />
      <el-table-column label="项目名称" align="center" prop="projectName">
    <template #default="scope">
        <router-link 
            :to="'/manager/requirement/data/index/' + scope.row.id"
            class="link-type">
            <span>{{ scope.row.projectName }}</span>
        </router-link>
    </template>
</el-table-column>
      <el-table-column label="所属部门" align="center" prop="deptId">
        <template #default="scope">
          <dict-tag :options="sys_dept" :value="scope.row.deptId"/>
        </template>
      </el-table-column>
     
            <el-table-column label="创建者" align="center" prop="creatorName">
        <template #default="scope">
          {{ scope.row.creatorName || '未知用户' }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="需求总数" align="center" prop="reqCount" width="100">
  <template #header>
    <span style="margin-right:8px">需求总数</span>
    <el-tooltip effect="dark" placement="top">
      <template #content>当前需求总数仅统计未完成的需求</template>
      <el-icon class="info-icon">
        <QuestionFilled />
      </el-icon>
    </el-tooltip>
  </template>
  <template #default="scope">
    {{ scope.row.reqCount }}
  </template>
</el-table-column>
       <el-table-column label="成员数" align="center" prop="calculatedMemberCount">
        <template #default="scope">
          {{ scope.row.calculatedMemberCount || 0 }}
        </template>
      </el-table-column>
     <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
  <template #default="scope">
    <el-space :size="8">
      <router-link 
        :to="'/manager/requirement/data/index/' + scope.row.id" 
        class="link-type"
        v-hasPermi="['manager:project:list']">
        <el-button link type="primary" icon="View">详情</el-button>
      </router-link>
      <el-button link type="warning" icon="User" @click="handleInvite(scope.row)" 
        v-hasPermi="['manager:project:edit']">邀请</el-button>
      <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" 
        v-hasPermi="['manager:project:edit']">修改</el-button>
      <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)"
        v-hasPermi="['manager:project:remove']">删除</el-button>
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

    <!-- 添加或修改项目管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="projectRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="业务标识" prop="projectId">
          <el-input v-model="form.projectId" placeholder="请输入业务标识" />
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-select v-model="form.deptId" placeholder="请选择所属部门">
            <el-option
              v-for="dict in sys_dept"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="邀请成员加入项目" v-model="data.inviteDialogVisible" width="600px" @close="handleInviteClose">
  <el-form label-width="80px">
    <el-form-item label="项目名称">
      <el-input :model-value="data.currentProject?.projectName" disabled/>
    </el-form-item>
     <!-- 已有成员展示 -->
    <el-form-item label="已有成员">
      <div class="existing-members" style="width: 100%">
        <el-scrollbar>
          <div class="select-style">
            <el-tag
              v-for="userId in data.currentProject?.userIds"
              :key="userId"
              :closable="false"
              class="custom-tag"
              disable-transitions
            >
              {{ getUserName(userId) }}
            </el-tag>
            <span v-if="!data.currentProject?.userIds?.length" class="placeholder">暂无成员</span>
          </div>
        </el-scrollbar>
      </div>
    </el-form-item>

    <el-form-item label="选择成员">
      <el-select 
        v-model="data.selectedUsers"
        multiple
        filterable
        placeholder="请选择要邀请的成员"
        style="width: 100%"
      >
        <el-option
  v-for="user in data.userList.filter(u => !data.currentProject?.userIds?.includes(u.userId))"
  :key="user.userId"
  :label="user.nickName"
  :value="user.userId"
/>
      </el-select>
    </el-form-item>
  </el-form>
  <template #footer>
    <el-button @click="data.inviteDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="handleInviteSubmit">邀 请</el-button>
  </template>
</el-dialog>
  </div>
</template>

<script setup name="Project">
import { listProject, getProject, delProject, addProject, updateProject } from "@/api/manager/project";
// 导入成员管理相关接口
import { listMember, getMember, delMember, addMember, updateMember } from "@/api/manager/member";
//导入用户管理相关接口
import { listUser, getUser, delUser, addUser, updateUser } from "@/api/manager/user";
//新增api/page
import { loadAllParams } from "@/api/page/page";
const { proxy } = getCurrentInstance();
const { sys_dept } = proxy.useDict('sys_dept');

const projectList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const memberList = ref([]);

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    deptId: null,
    projectName: null,
    creatorId: null,
  },
  rules: {
    deptId: [
      { required: true, message: "所属部门为空", trigger: "change" }
    ],
    projectName: [
      { required: true, message: "项目名称不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询项目管理列表 */
function getList() {
  loading.value = true;
  listProject(queryParams.value).then(response => {
    projectList.value = response.rows;
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
    projectId: null,
    deptId: null,
    projectName: null,
    creatorId: null,
    createTime: null,
    requirementCount: null,
    memberCount: null
  };
  proxy.resetForm("projectRef");
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
  title.value = "添加项目管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getProject(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改项目管理";
  });
}

// 邀请成员逻辑实现
function handleInvite(row) {
  data.currentProject = row;
  getUserList();
  
}

/** 邀请成员提交 */
function handleInviteSubmit() {
  if (!data.selectedUsers.length) {
    proxy.$modal.msgWarning("请至少选择一个成员");
    return;
  }
  
  const formData = {
    projectId: data.currentProject.id,
    userIds: data.selectedUsers
  };

  addMember(formData).then(response => {
    proxy.$modal.msgSuccess("邀请成功");
    data.inviteDialogVisible = false;
    getList(); // 刷新项目列表
    getMemberList(data.currentProject); // 刷新成员列表
  }).catch(error => {
    proxy.$modal.msgError("邀请失败");
  });
}

// 邀请对话框关闭回调
function handleInviteClose() {
  data.selectedUsers = []
  data.currentProject = null
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["projectRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateProject(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addProject(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除项目管理编号为"' + _ids + '"的数据项？').then(function() {
    return delProject(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/project/export', {
    ...queryParams.value
  }, `project_${new Date().getTime()}.xlsx`)
}

/** 查询项目成员列表 */
function getMemberList(row) {
  loading.value = true;
  listMember(row.value).then(response => {
    memberList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询可以邀请的用户信息列表 */
function getUserList() {
  // 加载用户列表
  loading.value = true;
  listUser(loadAllParams).then(response => {
    data.userList = response.rows;
    data.inviteDialogVisible = true; // 打开对话框
  }).catch(error => {
    proxy.$modal.msgError("用户加载失败");
  }).finally(() => {
    loading.value = false;
  });
}

// 在 methods 中添加用户ID转姓名方法
function getUserName(userId) {
  const user = data.userList.find(u => u.userId === userId) 
    || projectList.value.flatMap(p => p.userIds).find(u => u.userId === userId);
  return user?.nickName || `用户#${userId}`;
}

getList();

</script>

<style scoped>
/* 保持与 el-select 相同的样式 */
.select-style {
  width: 100%;
  min-height: 32px;
  padding: 1px 11px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  background-color: var(--el-disabled-bg-color);
  cursor: not-allowed;
}

.custom-tag {
  margin: 2px;
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-regular);
}

.placeholder {
  color: var(--el-text-color-placeholder);
  line-height: 30px;
}
</style>

<style scoped>


.info-icon {
  color: #909399;
  font-size: 14px;
  cursor: help;
  transition: color 0.2s;
}

.info-icon:hover {
  color: #409eff;
}
</style>