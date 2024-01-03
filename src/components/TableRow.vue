<template>
  <table class="container results">
    <thead>
    <tr>
      <th>X</th>
      <th>Y</th>
      <th>R</th>
      <th>Время</th>
      <th>Попадание</th>
    </tr>
    </thead>
    <tbody v-if="listOfPoints.length">
    <tr v-for="cPoint in listOfPoints">
      <td>{{cPoint.x}}</td>
      <td>{{cPoint.y}}</td>
      <td>{{cPoint.r}}</td>
      <td>{{cPoint.date}}</td>
      <td v-if="cPoint.result" class="green">попал</td>
      <td v-else class="red">мимо</td>
    </tr>

    </tbody>
    <tbody v-else>
    <tr>
      <td class="text-gray" colspan="4">Нет данных</td>
    </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  name: "TableRow",
  props: {
    listOfPoints: {
      type: Array,
      required: true,
    }
  }
}
</script>

<style scoped>

/* ====== Table ====== */

.results {
  flex-grow: 1;
}

.results tr:nth-child(odd) {
  background-color: #fff;
}

.results tr:nth-child(even) {
  background-color: #f5f5f5;
}

.results th {
  background-color: #44acff;
  font-weight: 500;
}

.results td, .results th {
  border-right: solid 2px #ededed;
  /* width: 14%; */
  max-width: 50px;
  padding: 5px 4px;
  text-align: center;
}

.results tr td:last-child, .results tr th:last-child {
  border-right: 0;
}

.results tr:first-child th:first-child {
  border-top-left-radius: 5px;
}

.results tr:first-child th:last-child {
  border-top-right-radius: 5px;
}

.results tr:last-child td:first-child {
  border-bottom-left-radius: 5px;
}

.results tr:last-child td:last-child {
  border-bottom-right-radius: 5px;
}

</style>