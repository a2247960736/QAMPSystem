<template>
  <div class="container">
    <!-- 日期选择部分 -->
    <div class="date-picker">
      <el-date-picker
        v-model="selectedMonth"
        type="month"
        value-format="YYYY-MM"
        @change="handleMonthChange"
      />
    </div>

    <!-- 日历部分 -->
    <div class="calendar">
      <!-- 星期标题 -->
      <div class="weekdays">
        <div v-for="day in weekdays" :key="day" class="weekday">{{ day }}</div>
      </div>

      <!-- 日期格子 -->
      <div v-for="(week, index) in calendarWeeks" :key="index" class="week">
        <div
          v-for="(date, i) in week"
          :key="i"
          class="day"
          :class="{ 'other-month': !date.isCurrentMonth }"
          @click="handleDayClick(date)"
        >
          <div class="day-number">{{ date.day }}</div>
          <div 
            v-if="date.work" 
            class="work-info"
            :class="[
              `status-${date.status}`,
              date.position
            ]"
          >
            {{ date.work }}
          </div>
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" title="日期信息" width="30%">
      <span>选中的日期：{{ selectedDate }}</span>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElDatePicker, ElDialog } from 'element-plus'

// 状态映射
const statusMap = {
  ongoing: '进行中',
  ended: '已结束',
  overdue: '已逾期',
  paused: '已暂停'
}

// 当前选择的月份（格式：YYYY-MM）
const selectedMonth = ref(new Date().toISOString().slice(0, 7))
// 日历数据
const calendarWeeks = ref([])
// 弹窗控制
const dialogVisible = ref(false)
// 选中的日期
const selectedDate = ref('')
// 模拟时间段数据
const mockPeriods = ref([
  {
    start: '2025-03-01',
    end: '2025-03-05',
    work: '项目会议',
    status: 'ongoing'
  },
  {
    start: '2025-03-10',
    end: '2025-03-12',
    work: '需求评审',
    status: 'ended'
  }
])

// 星期标题
const weekdays = ['一', '二', '三', '四', '五', '六', '日']

// 生成日历
const generateCalendar = (year, month) => {
  const weeks = []
  const firstDay = new Date(year, month - 1, 1)
  const lastDay = new Date(year, month, 0)
  
  // 调整第一天为周一
  let day = firstDay.getDay() === 0 ? 6 : firstDay.getDay() - 1
  let week = []
  
  // 添加上个月的日期
  for (let i = day; i > 0; i--) {
    const prevDate = new Date(year, month - 1, 1 - i)
    week.push(createDayObject(prevDate, false))
  }

  // 添加本月日期
  for (let d = 1; d <= lastDay.getDate(); d++) {
    const currentDate = new Date(year, month - 1, d)
    week.push(createDayObject(currentDate, true))
    if (week.length === 7) {
      weeks.push([...week])
      week = []
    }
  }

  // 添加下个月的日期
  while (week.length < 7) {
    const nextDate = new Date(year, month, week.length - day + 1)
    week.push(createDayObject(nextDate, false))
  }
  if (week.length) weeks.push(week)

  return weeks
}

// 创建日期对象
const createDayObject = (date, isCurrentMonth) => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`

  let workInfo = null
  let status = null
  let position = null

  for (const period of mockPeriods.value) {
    const periodStart = new Date(period.start)
    const periodEnd = new Date(period.end)
    const currentDate = new Date(dateStr)

    if (currentDate >= periodStart && currentDate <= periodEnd) {
      // 计算当月边界
      const currentMonthFirstDay = new Date(year, month - 1, 1)
      const currentMonthLastDay = new Date(year, month, 0)

      // 计算实际起止日期
      const actualStart = periodStart < currentMonthFirstDay ? currentMonthFirstDay : periodStart
      const actualEnd = periodEnd > currentMonthLastDay ? currentMonthLastDay : periodEnd

      // 确定位置状态
      if (currentDate.getTime() === actualStart.getTime()) {
        position = 'start'
      } else if (currentDate.getTime() === actualEnd.getTime()) {
        position = 'end'
      } else {
        position = 'middle'
      }

      workInfo = `${period.work}--${statusMap[period.status]}`
      status = period.status
      break
    }
  }

  return {
    day: day,
    date: dateStr,
    isCurrentMonth,
    work: workInfo,
    status: status,
    position: position
  }
}

// 月份变更处理
const handleMonthChange = () => {
  const [year, month] = selectedMonth.value.split('-')
  calendarWeeks.value = generateCalendar(parseInt(year), parseInt(month))
}

// 日期点击处理
const handleDayClick = (date) => {
  selectedDate.value = date.date
  dialogVisible.value = true
}

// 初始化日历
handleMonthChange()
</script>

<style scoped>
.container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.date-picker {
  margin-bottom: 20px;
}

.calendar {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  padding: 10px 0;
  color: #909399;
}

.week {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  min-height: 100px;
  position: relative;
}

.day {
  border: 1px solid #ebeef5;
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  z-index: 1;
}

.day:hover {
  background-color: #f5f7fa;
}

.other-month {
  color: #c0c4cc;
  background-color: #fafafa;
}

.day-number {
  font-weight: bold;
  margin-bottom: 4px;
}

.work-info {
  font-size: 12px;
  padding: 4px;
  color: white;
  display: block;
  position: relative;
  z-index: 2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 状态颜色 */
.status-ongoing { background-color: #409eff; }
.status-ended { background-color: #67c23a; }
.status-overdue { background-color: #f56c6c; }
.status-paused { background-color: #e6a23c; }

/* 连续高亮效果 */
.start,
.middle,
.end {
  margin: 0;
  border-radius: 12px;
}
</style>