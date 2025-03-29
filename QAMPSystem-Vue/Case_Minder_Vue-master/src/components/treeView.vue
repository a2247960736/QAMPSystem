<template>
  <div class="tree-container">
    <el-tree 
      :data="treeData"
      node-key="id"
      :props="defaultProps"
      default-expand-all
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node }" class="custom-node">
        <i :class="node.children ? 'el-icon-folder' : 'el-icon-document'"></i>
        <span>{{ node.label }}</span>
      </span>
    </el-tree>
  </div>
</template>

<script>
export default {
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 临时示例数据
      treeData: [{
        id: 1,
        label: '我的空间',
        children: [{
          id: 2,
          label: '示例用例集',
          children: []
        }]
      }]
    }
  },
  methods: {
    handleNodeClick(data) {
      console.log('节点点击:', data)
    }
  }
}
</script>

<style scoped>
.tree-container {
  height: 100%;
  padding: 10px;
  overflow-y: auto;
}

.custom-node {
  font-size: 14px;
  
  i {
    margin-right: 8px;
    color: #e6a23c;
    
    &.el-icon-document {
      color: #409eff;
    }
  }
}

/* 调整树节点高度 */
/* 调整树节点高度 */
::v-deep .el-tree-node__content {
  height: 36px;
}
</style>
