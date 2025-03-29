<template>
  <div class="app-container">
    
  </div>
</template>

<script setup name="Case">
import { listCase, getCase, delCase, addCase, updateCase, getCaseGeneralInfo, getCountByCondition, 
  listCreators, getCaseDetail, deleteCaseById, editCase, createOrCopyCase, delCase
 } from "@/api/manager/case";

const { proxy } = getCurrentInstance();

const caseList = ref([]);
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
    description: [
      { required: true, message: "用例描述不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询测试用例列表 */
function getList() {
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

/** 提交按钮 */
function submitForm() {
  proxy.$refs["caseRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateCase(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addCase(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除测试用例编号为"' + _ids + '"的数据项？').then(function() {
    return delCase(_ids);
  }).then(() => {
    getList();
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
</script>
