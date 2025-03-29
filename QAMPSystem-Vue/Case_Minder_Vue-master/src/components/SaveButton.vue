<template>
  <button @click="saveData">保存</button>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: 'SaveButton',
  methods: {
    ...mapActions([
      'setMemory'
    ]),
    saveData() {
      const mindMapData = this.getMindMapData();
      console.log(window.minder.exportJson());
      const data = {
        key: 'mindText',
        value: mindMapData
      };
      this.setMemory(data);
    },
    getMindMapData() {
      // 假设 this.mindMapInstance 是 kityminder-core 的实例
       if (window.minder) {
        return window.minder.exportJson();
      }
      return {};
    }
  },
  mounted() {
    // 在组件挂载时初始化脑图实例，这里需要根据实际情况修改
    // 假设你在某个地方创建了 kityminder-core 的实例并赋值给 this.mindMapInstance
    // this.mindMapInstance = new KityMinder.Core.Minder();
    // this.mindMapInstance.init(domElement);
  }
};
</script>
