<template>
<div>
  <el-row :gutter="10" style="margin-bottom: 40px">
    <el-col :span="6">
      <el-card style="color: #409eff">
        <div><i class="el-icon-user-solid"/> 用户总数</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold;">
          100
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #67c23a">
        <div><i class="el-icon-money"/>销售总量</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold;">
          ￥1000
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #e6a23c">
        <div><i class="el-icon-bank-card"/>收益总额</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold;">
          ￥30000
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #f56c6c">
        <div><i class="el-icon-s-shop"/>门店总数</div>
        <div style="padding: 10px 0; text-align: center; font-weight: bold;">
          20
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="12">
      <div id="main" style="width: 500px; height: 400px"></div>
    </el-col>
    <el-col :span="12">
      <div id="pie" style="width: 500px; height: 400px"></div>
    </el-col>
  </el-row>

</div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {

    }
  },
  // 页面div元素渲染(创建好)之后才会触发;而created直接就触发，不管页面是否渲染好
  mounted() {
    var option = {
      title: {
        text: '各季度用户注册数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
          // 类似格式，可以容纳不同的数据图
        {
          // name一致，二者颜色就一致
          name: '折线图',
          data: [],
          type: 'line'
        },
        {
          name: '条形图',
          data: [],
          type: 'bar'
        }
      ]
    };

    // 饼图
    var pieOption = {
      title: {
        text: '各季度用户注册数量统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '70%',
          label: {
            normal: {
              show: true,
              // 标签的位置
              position: 'inner',
              textStyle: {
                fontWeight: 300,
                // 文字的字体大小
                fontSize: 16,
                color: "#fff",
              },
              formatter: '{d}%'
            }
          },
          // 填空
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/members").then(res => {
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data
      // 在数据准备完毕之后，再set
      myChart.setOption(option);

      pieOption.series[0].data = [
        { name: "第一季度", value: res.data[0] },
        { name: "第二季度", value: res.data[1] },
        { name: "第三季度", value: res.data[2] },
        { name: "第四季度", value: res.data[3] }
      ]
      pieChart.setOption(pieOption);
    })
  },
}
</script>

<style scoped>

</style>