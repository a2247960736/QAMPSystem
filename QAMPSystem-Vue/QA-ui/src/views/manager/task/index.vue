<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务优先级" prop="priority" >
        <el-select v-model="queryParams.priority" placeholder="请选择任务优先级" clearable>
          <el-option
            v-for="dict in task_priority"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
          <el-option
            v-for="dict in task_status"
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
          v-hasPermi="['manager:task:add']"
        >新增</el-button>
      </el-col>
   
    
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="filteredTaskList" @selection-change="handleSelectionChange">
       <el-table-column label="序号" align="center" width="80">
    <template #default="scope">
      {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
    </template>
  </el-table-column>
      <el-table-column label="任务名称" align="center" prop="taskName" width="300"/>
      
      
      <el-table-column label="任务开始时间" align="center" prop="startTime" width="130">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务结束时间" align="center" prop="endTime" width="130">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工时" align="center" prop="estimatedHours" width="50"/>
      <el-table-column 
  label="任务优先级" 
  align="center" 
  prop="priority"
  width="120"
  sortable
>
  <template #default="scope">
    <el-tag 
      :type="{'HIGH':'danger', 'MEDIUM':'warning', 'LOW':'success'}[scope.row.priority]"
      effect="dark"
    >
      {{ task_priority.find(v => v.value === scope.row.priority)?.label || 'N/A' }}
    </el-tag>
  </template>
</el-table-column>

<el-table-column 
  label="任务状态" 
  align="center" 
  prop="status"
  width="150"
  sortable
>
  <template #default="scope">
    <el-tag
      :type="{
        'NOT_STARTED':'info',
        'IN_PROGRESS':'',
        'OVERDUE':'danger',
        'COMPLETED':'success',
        'PAUSED':'warning'
      }[scope.row.status]"
      :effect="scope.row.status === 'IN_PROGRESS' ? 'plain' : 'light'"
    >
      {{ task_status.find(v => v.value === scope.row.status)?.label || 'N/A' }}
    </el-tag>
  </template>
</el-table-column>
     <el-table-column 
  label="操作" 
  align="center" 
  class-name="small-padding fixed-width" 
  width="200"
>
  <template #default="scope">
    <el-space :size="25">
     
      <el-button 
        link 
        type="primary" 
        icon="Edit" 
        @click="handleUpdate(scope.row)"
        v-hasPermi="['manager:task:edit']">修改</el-button>
      <el-button 
        link 
        type="danger" 
        icon="Delete" 
        @click="handleDelete(scope.row)"
        v-hasPermi="['manager:task:remove']">删除</el-button>
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

    <!-- 添加或修改个人任务管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="taskRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称"  style="width: 230px;float: right;"/>
        </el-form-item>
        <el-form-item label="任务优先级" prop="priority">
          <el-select v-model="form.priority" placeholder="请选择">
            <el-option
              v-for="dict in task_priority"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option
              v-for="dict in task_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="计划开始时间" prop="startTime" >
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="计划结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="分配工时" prop="estimatedHours">
  <div style="display: flex;align-items: center;gap: 10px">
    <el-input 
      v-model="form.estimatedHours" 
      placeholder="请输入"
      :max="maxHours"
      style="width: 230px;"
      type="number"
      :min="0"
      @input="handleHoursInput"
    >
      <template #append>小时</template>
    </el-input>
    <el-text v-if="maxHours > 0" type="info" size="small">
      <el-icon><InfoFilled /></el-icon>
      可用工时 {{ maxHours }}h
    </el-text>
  </div>
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

<script setup name="Task">
import { listTask, getTask, delTask, addTask, updateTask } from "@/api/manager/task";

const { proxy } = getCurrentInstance();
const { task_status, task_priority } = proxy.useDict('task_status', 'task_priority');

const taskList = ref([]);
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
    taskName: null,
    priority: null,
    status: null,
    creatorId: null,
    startTime: null,
    endTime: null,
  },
  rules: {
    taskName: [
      { required: true, message: "任务名称不能为空", trigger: "blur" }
    ],
    priority: [
      { required: true, message: "任务优先级不能为空", trigger: "change" }
    ],
    status: [
      { required: true, message: "任务状态不能为空", trigger: "change" }
    ],
    startTime: [
  { required: true, message: "请选择开始时间", trigger: "change" }
],
endTime: [
  { required: true, message: "请选择结束时间", trigger: "change" }
]
  },
  estimatedHours: [
      { 
        validator: (_, value, callback) => {
          if (value > maxHours.value) {
            callback(new Error(`最大允许工时：${maxHours.value}小时`));
          } else {
            callback();
          }
        },
        trigger: ['blur', 'change']
      }
    ]
});

const { queryParams, form, rules } = toRefs(data);

//
const filteredTaskList = ref([]);
/** 查询个人任务管理列表 */
function getList() {
  loading.value = true;
  listTask(queryParams.value).then(response => {
    taskList.value = response.rows;
    // 创建过滤后的副本
    filteredTaskList.value = taskList.value.filter(task => {
      const now = new Date();
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      
      if (task.status === 'COMPLETED' && task.endTime) {
        const endDate = new Date(task.endTime);
        const taskEndDate = new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
        return today <= taskEndDate;
      }
      return true;
    });
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
    taskName: null,
    priority: null,
    status: null,
    creatorId: null,
    createTime: null,
    updateTime: null,
    startTime: null,
    endTime: null,
    estimatedHours: null,
    actualHours: null
  };
  originalStatus.value = null;
  proxy.resetForm("taskRef");
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
  title.value = "添加个人任务管理";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
   const _id = row.id || ids.value
  getTask(_id).then(response => {
    form.value = response.data;
    originalStatus.value = response.data.status; // 保存原始状态
    open.value = true;
    title.value = "修改个人任务管理";
  });
}

/** 提交按钮 */
/** 提交按钮 */
async function submitForm() {
  try {
    const valid = await proxy.$refs["taskRef"].validate();
    if (!valid) return;

    // 状态校验逻辑
    if (form.value.status === 'COMPLETED' && originalStatus.value !== 'COMPLETED') {
      await proxy.$modal.confirm('任务已完成后将无法再修改，确认要提交吗？');
    }

    // 统一使用await处理异步
    const apiCall = form.value.id ? updateTask(form.value) : addTask(form.value);
    const res = await apiCall;
    
    proxy.$modal.msgSuccess(form.value.id ? "修改成功" : "新增成功");
    open.value = false;
    getList();
  } catch (error) {
    // 用户点击取消或请求失败时静默处理
    if (error !== 'cancel') {
      console.error('提交失败:', error);
    }
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除"「' + row.taskName + '」"任务？').then(function() {
    return delTask(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/task/export', {
    ...queryParams.value
  }, `task_${new Date().getTime()}.xlsx`)
}

getList();

//新增修改任务计算可用工时
// 新增响应式变量
const maxHours = ref(0);

// 计算日期差的方法
const calculateDays = (start, end) => {
  if (!start || !end) return 0;
  const startDate = new Date(start);
  const endDate = new Date(end);
  if (startDate > endDate) return 0;
  
  let count = 0;
  const current = new Date(startDate);
  while (current <= endDate) {
    const weekDay = current.getDay();
    if (weekDay !== 0 && weekDay !== 6) { // 排除周日(0)和周六(6)
      count++;
    }
    current.setDate(current.getDate() + 1);
  }
  return count;
};

// 修改监听逻辑处理开始时间晚于结束时间的情况
watch([() => form.value.startTime, () => form.value.endTime], ([newStart, newEnd]) => {
  if (newStart && newEnd) {
    const days = calculateDays(newStart, newEnd);
    maxHours.value = days * 8;
    
    // 当开始时间晚于结束时间时清空并重置
    if (new Date(newStart) > new Date(newEnd)) {
      form.value.startTime = null;
      form.value.endTime = null;
      maxHours.value = 0;
      proxy.$modal.msgWarning("开始时间不能晚于结束时间");
      return;
    }

    if (form.value.estimatedHours > maxHours.value) {
      form.value.estimatedHours = maxHours.value;
    }
  } else {
    maxHours.value = 0;
  }
});

//修改任务状态为已完成是，提醒用户确认
// 新增变量存储原始状态
const originalStatus = ref(null);

const handleHoursInput = (value) => {
  if (Number(value) > maxHours.value) {
    form.value.estimatedHours = Math.min(Number(value), maxHours.value);
  }
};



</script>
