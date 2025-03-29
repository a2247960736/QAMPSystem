<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="计划标识" prop="planId">
        <el-input
          v-model="queryParams.planId"
          placeholder="请输入计划标识"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
            <el-form-item label="关联需求" prop="reqId">
        <el-select
          v-model="queryParams.reqId"
          placeholder="请选择关联需求"
          filterable
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="item in requirementOptions"
            :key="item.id"
            :label="`${item.title} (${item.reqId})`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="计划名称" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入计划名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      
<el-form-item 
  label="测试负责人" 
  prop="leaderId" 
  class="long-label"
>
  <el-select
    v-model="queryParams.leaderId"
    placeholder="请选择测试负责人"
    filterable
    clearable
    style="width: 200px"
  >
    <!-- 选项代码保持不变 -->
  </el-select>
</el-form-item>
      <el-form-item label="计划状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择计划状态" clearable>
          <el-option
            v-for="dict in plan_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="提测时间" prop="submitTime">
        <el-date-picker clearable
          v-model="queryParams.submitTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择提测时间">
        </el-date-picker>
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
          v-hasPermi="['manager:plan:add']"
        >新增测试计划</el-button>
      </el-col>

      
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    
    <el-table 
  v-loading="loading" 
  :data="planList" 
  @selection-change="handleSelectionChange"
  stripe
  fit
  style="width: 100%"
>
  
  <el-table-column 
    label="计划标识" 
    align="center" 
    prop="planId" 
    min-width="90"
    :show-overflow-tooltip="true"
  />
  
  <el-table-column 
    label="计划名称" 
    align="center" 
    prop="planName" 
    min-width="120"
    :show-overflow-tooltip="true"
  />
  
  <el-table-column 
    label="关联需求" 
    align="center" 
    min-width="140"
    :show-overflow-tooltip="true"
  >
    <template #default="scope">
      <span class="link-type">{{ scope.row.title }}</span>
    </template>
  </el-table-column>

  <el-table-column 
    label="提测" 
    align="center" 
    min-width="80"
  >
    <template #default="scope">
      <span v-if="scope.row.submitterId">{{ scope.row.devName }}</span>
      <span v-else style="color: var(--el-text-color-secondary)">-</span>
    </template>
  </el-table-column>

  <el-table-column 
    label="提测状态" 
    align="center" 
    min-width="100"
  >
    <template #default="scope">
      <el-tag 
        :type="scope.row.submitterId ? 'success' : 'info'" 
        size="medium"
        effect="plain"
      >
        {{ scope.row.submitterId ? '已提测' : '未提测' }}
      </el-tag>
    </template>
  </el-table-column>

  <el-table-column 
    label="测试负责人" 
    align="center" 
    prop="testName" 
    min-width="80"
    :show-overflow-tooltip="true"
  />

  <el-table-column label="计划状态" align="center" prop="status" width="110" sortable>
    <template #default="scope">
      <el-tag
        :type="{
          '1':'primary',
          '2':'success',
          '3':'warning',
          '4':'danger'
        }[scope.row.status]"
        effect="light"
      >
        <dict-tag :options="plan_status" :value="scope.row.status"/>
      </el-tag>
    </template>
  </el-table-column>

  <el-table-column label="提测时间" align="center" prop="submitTime" width="100" sortable>
    <template #default="scope">
      <span v-if="scope.row.submitTime" class="time-text">
        {{ parseTime(scope.row.submitTime, '{y}-{m}-{d}') }}
      </span>
      <span v-else style="color: #909399">-</span>
    </template>
  </el-table-column>

  <el-table-column 
  label="操作" 
  align="center" 
  width="120"
  fixed="right"
>
  <template #default="scope">
    <div class="action-container">
      <!-- 新增提测按钮 -->
      <el-tooltip content="提测" placement="top">
        <el-button 
          v-if="!scope.row.submitterId"
          link 
          type="warning" 
          :icon="Promotion"
          @click="handleSubmit(scope.row)"
          v-hasPermi="['manager:plan:submit']"
          class="action-btn"
        />
      </el-tooltip>

      <!-- 打回按钮 -->
      <el-tooltip content="打回" placement="top">
        <el-button 
          v-if="scope.row.submitterId"
          link 
          type="warning" 
          :icon="CircleClose"
          @click="handleUpdate(scope.row)"
          v-hasPermi="['manager:plan:edit']"
          class="action-btn"
        />
      </el-tooltip>
      
      <!-- 修改按钮 -->
      <el-tooltip content="修改" placement="top">
        <el-button 
          link 
          type="primary" 
          :icon="Edit"
          @click="handleUpdate(scope.row)"
          v-hasPermi="['manager:plan:edit']"
          class="action-btn"
        />
      </el-tooltip>
      
      <!-- 删除按钮 -->
      <el-tooltip content="删除" placement="top">
        <el-button 
          link 
          type="danger" 
          :icon="Delete"
          @click="handleDelete(scope.row)"
          v-hasPermi="['manager:plan:remove']"
          class="action-btn"
        />
      </el-tooltip>
    </div>
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

    

    <!-- 添加或修改测试计划对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
  <el-form ref="planRef" :model="form" :rules="rules" label-width="108px" class="compact-form">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="计划标识：" prop="planId">
          <el-input v-model="form.planId" placeholder="如 PLAN-001" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="计划名称：" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="关联需求：" prop="reqId">
      <el-select 
        v-model="form.reqId"
        placeholder="请选择需求"
        filterable
        style="width: 100%"
      >
        <el-option
          v-for="item in requirementOptions"
          :key="item.id"
          :label="`${item.title} (${item.reqId})`"
          :value="item.id"
        />
      </el-select>
    </el-form-item>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="提测人员：" prop="submitterId">
          <el-select 
            v-model="form.submitterId" 
            placeholder="请选择"
            filterable
          >
            <el-option
              v-for="user in devUserList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="测试负责人：" prop="leaderId" >
          <el-select 
            v-model="form.leaderId" 
            placeholder="请选择"
            filterable
          >
            <el-option
              v-for="user in qaUserList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="抄送邮箱：" prop="ccEmails">
  <el-input
    v-model="form.ccEmails"
    type="textarea"
    placeholder="请输入邮箱，多个邮箱用逗号分隔"
    :autosize="{ minRows: 2, maxRows: 4 }"
    style="width: 100%"
  />
  <div class="form-tips">示例：user1@example.com, user2@company.cn</div>
</el-form-item>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="计划状态：" prop="status">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option
              v-for="dict in plan_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="提测时间：" prop="submitTime">
          <el-date-picker
            v-model="form.submitTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <template #footer>
    <div class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </template>
</el-dialog>

<!-- 提测计划对话框 -->
    <el-dialog :title="submitTitle" v-model="submitopen" width="600px" append-to-body>
  <el-form ref="submitRef" :model="form" :rules="submitrules" label-width="100px" class="compact-form">
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="提测人员：" prop="submitterId">
          <el-select 
            v-model="form.submitterId" 
            placeholder="请选择"
            filterable
          >
            <el-option
              v-for="user in devUserList"
              :key="user.userId"
              :label="user.nickName"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="计划状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择计划状态" clearable>
          <el-option
            v-for="dict in plan_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      </el-col>
    </el-row>

    

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="提测时间：" prop="submitTime">
          <el-date-picker
            v-model="form.submitTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="选择日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <template #footer>
    <div class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="SubmitCancel">取 消</el-button>
    </div>
  </template>
</el-dialog>
  </div>
</template>

<script setup name="Plan">
//导入用户管理相关接口
import { listUser,devListUser,qaListUser, getUser, delUser, addUser, updateUser } from "@/api/manager/user";
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/manager/plan";
//新增api/page
import { loadAllParams } from "@/api/page/page";
// 引入需求相关接口
import { listRequirement, getRequirement, delRequirement, addRequirement, updateRequirement } from "@/api/manager/requirement";
// 在图标导入部分新增 CircleClose
import {  Promotion,CircleClose,Edit,Delete } from '@element-plus/icons-vue'

const { proxy } = getCurrentInstance();
const { plan_status } = proxy.useDict('plan_status');

const open = ref(false);
const submitopen = ref(false);
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
    planId: null,
    reqId: null,
    planName: null,
    submitterId: null,
    leaderId: null,
    ccEmails: null,
    status: null,
    submitTime: null
  },
  rules: {
    planId: [
      { required: true, message: "计划标识不能为空", trigger: "blur" }
    ],
    reqId: [
      { required: true, message: "关联需求不能为空", trigger: "blur" }
    ],
    planName: [
      { required: true, message: "计划名称不能为空", trigger: "blur" }
    ],
    leaderId: [
      { required: true, message: "测试负责人不能为空", trigger: "blur" }
    ],
    ccEmails: [
      { required: true, message: "抄送邮箱不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "计划状态不能为空", trigger: "change" }
    ],
    ccEmails: [
    { 
      validator: (rule, value, callback) => {
        const emailReg = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
        const emails = value ? value.split(',').map(e => e.trim()) : []
        
        if (emails.some(email => !emailReg.test(email))) {
          callback(new Error("包含无效邮箱格式或逗号分隔不正确"));
        } else {
          callback();
        }
      },
      trigger: 'blur' 
    }
  ]
  },
   submitrules: {
    submitterId: [  
      { 
        required: true,
        message: "提测人员不能为空",
        trigger: ['blur', 'change']
      }
    ],
    status: [
      { required: true, message: "计划状态不能为空", trigger: "change" }
    ],
    submitTime: [
      { 
        required: true,
        message: "提测时间不能为空",
        trigger: ['blur', 'change']
      }
    ]
  }
});
const { queryParams, form, rules } = toRefs(data);
const planList = ref([]);
/** 查询测试计划列表 */
function getList() {
  loading.value = true;
  listPlan(queryParams.value).then(response => {
    planList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 取消按钮
function SubmitCancel() {
  submitopen.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    planId: null,
    reqId: null,
    planName: null,
    submitterId: null,
    leaderId: null,
    ccEmails: null,
    status: null,
    createTime: null,
    updateTime: null,
    submitTime: null
  };
  proxy.resetForm("planRef");
  proxy.resetForm("submitRef");
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
  title.value = "添加测试计划";
}


/** 提测按钮操作 */
function handleSubmit(row) {
 reset();
  const _id = row.id || ids.value;
  
  // 确保需求列表加载完成
  if (requirementOptions.value.length === 0) {
    getRequirementList().then(() => {
      loadSubMitPlanDetail(_id);
    });
  } else {
    loadSubMitPlanDetail(_id);
  }
}


const submitTitle = ref("提测操作");
// 新增加载详情方法
function loadSubMitPlanDetail(id) {
 getPlan(id).then(response => {
    // 直接使用需求表主键id
    form.value = { 
      ...response.data,
      reqId: response.data.reqId // 保持原字段名但实际存储需求表id
    };
    submitopen.value = true;
  });
}


/** 修改按钮操作 */
function handleUpdate(row) {
 reset();
  const _id = row.id || ids.value;
  
  // 确保需求列表加载完成
  if (requirementOptions.value.length === 0) {
    getRequirementList().then(() => {
      loadPlanDetail(_id);
    });
  } else {
    loadPlanDetail(_id);
  }
}

// 新增加载详情方法
function loadPlanDetail(id) {
 getPlan(id).then(response => {
    // 直接使用需求表主键id
    form.value = { 
      ...response.data,
      reqId: response.data.reqId // 保持原字段名但实际存储需求表id
    };
    open.value = true;
    title.value = "修改测试计划";
  });
}

/** 提交按钮 */
function submitForm() {
  // 根据不同的对话框使用不同的 ref
  const formRef = open.value ? 'planRef' : 'submitRef';
  proxy.$refs[formRef].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updatePlan(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          submitopen.value = false;
          getList();
        });
      } else {
        addPlan(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除"' + row.planName + '"的计划？').then(function() {
    return delPlan(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/plan/export', {
    ...queryParams.value
  }, `plan_${new Date().getTime()}.xlsx`)
}

getList();
getRequirementList();
//查询所有需求
/** 查询需求管理列表 */
const requirementOptions = ref([]);
const requirementLoading = ref(true);
const requirementShowSearch = ref(true);
function getRequirementList() {
  loading.value = true;
  listRequirement(loadAllParams).then(response => {
    requirementOptions.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

//获取开发部门人员名单
/** 查询可以邀请的用户信息列表 */
const devUserList = ref([]);
function getDevUserList() {
  // 加载用户列表
  loading.value = true;
  devListUser(loadAllParams).then(response => {
    devUserList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

//获取测试部门人员名单
/** 查询可以邀请的用户信息列表 */
const qaUserList = ref([]);
function getQAUserList() {
  // 加载用户列表
  loading.value = true;
  qaListUser(loadAllParams).then(response => {
    qaUserList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 新增提测人员选择监听
watch(
   () => form.value.submitterId,
  (newVal) => {
    if (newVal) {
      // 当提测人员存在时，自动设置状态为已提测（假设'2'是已提测状态值）
      // form.value.status = '2';
      // 自动设置当前时间为提测时间（可选）
      form.value.submitTime = proxy.parseTime(new Date(), '{y}-{m}-{d}');
    } else {
      form.value.status = '';
      form.value.submitTime = null;
    }
  },
  { immediate: true }
);

 getDevUserList();
 getQAUserList();

</script>
<style>
.long-label .el-form-item__label {
  min-width: 84px;
  letter-spacing: 0.5px;
  white-space: nowrap;
}

.time-text {
  font-family: monospace;
  letter-spacing: 0.5px;
}
.link-type {
  color: var(--el-color-primary);
  cursor: pointer;
}
.link-type:hover {
  text-decoration: underline;
}


.action-container {
  display: flex;
  flex-wrap: wrap;
  gap: 1px;
  padding: 4px 0;
  justify-content: center;
}

.action-btn {
  margin: 0;
  padding: 8px;
  flex-shrink: 0;
}
</style>
