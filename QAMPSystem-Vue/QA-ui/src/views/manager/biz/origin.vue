<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="业务线名称" prop="productLineId">
        <el-input
          v-model="queryParams.productLineId"
          placeholder="请输入业务线名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="渠道" prop="channel">
        <el-input
          v-model="queryParams.channel"
          placeholder="请输入渠道"
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
          v-hasPermi="['manager:biz:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manager:biz:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manager:biz:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['manager:biz:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bizList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件夹主键" align="center" prop="id" />
      <el-table-column label="业务线名称" align="center" prop="productLineId" />
      <el-table-column label="文件数内容" align="center" prop="content" />
      <el-table-column label="渠道" align="center" prop="channel" />
      <el-table-column label="逻辑删除" align="center" prop="isDelete" />
      <el-table-column label="创建时间" align="center" prop="gmtCreated" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.gmtCreated, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="gmtModified" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.gmtModified, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['manager:biz:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['manager:biz:remove']">删除</el-button>
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

    <!-- 添加或修改文件夹对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="bizRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件数内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="逻辑删除" prop="isDelete">
          <el-input v-model="form.isDelete" placeholder="请输入逻辑删除" />
        </el-form-item>
        <el-form-item label="创建时间" prop="gmtCreated">
          <el-date-picker clearable
            v-model="form.gmtCreated"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="gmtModified">
          <el-date-picker clearable
            v-model="form.gmtModified"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择更新时间">
          </el-date-picker>
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

<script setup name="Biz">
import { listBiz, getBiz, delBiz, addBiz, updateBiz, renameDir, deleteDir, getDirCardTree, moveDir    } from "@/api/manager/biz";

const { proxy } = getCurrentInstance();

const bizList = ref([]);
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
    productLineId: null,
    content: null,
    channel: null,
  },
  rules: {
    productLineId: [
      { required: true, message: "业务线名称不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "文件数内容不能为空", trigger: "blur" }
    ],
    channel: [
      { required: true, message: "渠道不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询文件夹列表 */
function getList() {
  loading.value = true;
  listBiz(queryParams.value).then(response => {
    bizList.value = response.rows;
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
    productLineId: null,
    content: null,
    channel: null,
    isDelete: null,
    gmtCreated: null,
    gmtModified: null
  };
  proxy.resetForm("bizRef");
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
  title.value = "添加文件夹";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getBiz(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改文件夹";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["bizRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateBiz(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addBiz(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除文件夹编号为"' + _ids + '"的数据项？').then(function() {
    return delBiz(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('manager/biz/export', {
    ...queryParams.value
  }, `biz_${new Date().getTime()}.xlsx`)
}

getList();
</script>
