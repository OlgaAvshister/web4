<template>
    <div class="wrapper">
        <div class="content">
            <div class="links">
                <h2>Укажите параметры X, Y и R</h2>
                <a href="/" v-on:click.prevent="logoutRequest" class="logout">Выйти</a>
            </div>
            <div class="content-wrapper">
                <div class="graph-container container">
                    <div class="graph">
                        <div class="figures">
                            <canvas class="figure quad2" width="150" height="150"></canvas>
                            <canvas class="figure quad1" width="150" height="150"></canvas>
                            <canvas class="figure quad3" width="150" height="150"></canvas>
                            <canvas class="figure quad4" width="150" height="150"></canvas>
                        </div>
                        <canvas v-on:click="addPointFromCanvas" id="plot" width="300" height="300"></canvas>
                    </div>
                </div>

                <div class="container selection">
                    <form v-on:submit.prevent="addPoint">
                        <div class="form-select x-select">
                            <label class="selection-label">Выберите X
                                <span :class="{'visible':!xValid}" class="warning"
                                      data-validate="Выберите X"></span>
                            </label>
                            <div class="select-buttons">
                                <button v-for="num in buttons" v-bind:key="num" v-model="x" @click="changeButton"
                                        class="changeX" type="button">{{num}}
                                </button>
                            </div>
                        </div>
                        <div class="form-select y-select">
                            <!--suppress XmlInvalidId -->
                            <label class="selection-label" for="Y-select">Введите Y
                                <span :class="{'visible':!yValid}" class="warning"
                                      data-validate="Y - значение в диапазоне [-3; 3]"></span>
                            </label>
                            <number-input v-model="y" @change="validateY" :step="0.001" :attrs="{id: 'Y-select'}"
                                          type="text" placeholder="-3...3" name="y" size="small"
                                          autocomplete="off"></number-input>
                        </div>
                        <div class="form-select r-select">
                            <label class="selection-label">Выберите R
                                <span :class="{'visible':!rValid}" class="warning"
                                      data-validate="R - положительное число"></span>
                            </label>
                            <div class="select-buttons">
                                <button v-for="num in buttons" v-bind:key="num" v-bind:class="{'btn-hold':(num === 1)}"
                                        v-model="r" @click="changeButton($event); reDrawGraph()" class="changeR"
                                        type="button">{{num}}
                                </button>
                            </div>
                        </div>
                        <div class="form-buttons">
                            <input class="form-btn btn-submit" type="submit" value="Отправить">
                        </div>
                    </form>
                </div>

              <TableRow :list-of-points=listOfPoints></TableRow>
            </div>
        </div>
    </div>
</template>

<script>
    import TableRow from '@/components/TableRow.vue'
    export default {
        name: "Main",
        components: {TableRow},
        data() {
            return {
                response: null,
                listOfPoints: [],
                buttons: [-5,-4, -3, -2, -1, 0, 1, 2, 3],
                x: null,
                y: null,
                r: 1,
                xValid: true,
                yValid: true,
                rValid: true,
                graph: {
                    1: {figure: 'arc', size: '1'},
                    2: {figure: 'empty', size: '1'},
                    3: {figure: 'rect', size: '1'},
                    4: {figure: 'triangle', size: '0.5'},
                },
            }
        },
        methods: {
            logoutRequest: function () {
                this.$axios({
                    method: 'post',
                    url: this.$BaseURL + 'logout',
                }).then(response => {
                    this.response = response;
                    localStorage.removeItem('currentUser');
                    this.$router.push('/');
                }).catch(error => {
                    console.log(error);
                });
            },
            getPointsRequest: function () {
                let base64Credential = localStorage.getItem('currentUser');
                return this.$axios({
                    method: 'get',
                    url: this.$BaseURL + 'points',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'Authorization': base64Credential
                    }
                }).then(response => {
                    if (response.data.length) {
                        this.listOfPoints.splice(0, this.listOfPoints.length, ...response.data);
                    }
                }).catch(error => {
                    console.log(error.response.data);
                });
            },
            addPointRequest: function (x, y, r) {
                let base64Credential = localStorage.getItem('currentUser');
                return this.$axios({
                    method: 'post',
                    url: this.$BaseURL + 'points',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json',
                        'Authorization': base64Credential
                    },
                    data: {x: x, y: y, r: r}
                }).then(response => {
                    let result = response.data.result;
                    let date = response.data.date;
                    this.drawDefinedPoint(x, y, r, result);
                    this.listOfPoints.unshift({x: x, y: y, r: r, result: result, date: date});
                    this.response = response;
                }).catch(error => {
                    console.log(error.response.data);
                    return false;
                });
            },
            addPointFromCanvas: function (e) {
                const point = this.getPointCoordinates(e);
                if (this.rValid) {
                    this.addPointRequest(point.x, point.y, this.r);
                }
            },
            addPoint: function () {
                if (this.x == null) {
                    this.xValid = false
                }
                if (this.y == null) {
                    this.yValid = false
                }
                if (this.r == null) {
                    this.rValid = false
                }

                if (this.xValid && this.yValid && this.rValid) {
                    this.addPointRequest(this.x, this.y, this.r);
                }
            },
            drawDefinedPoint: function (x, y, r, result) {
                const plot_canvas = document.querySelector("#plot");
                const context = plot_canvas.getContext("2d");
                context.beginPath();
                context.arc(x * 28*this.r/r + 150, 150 - 28 * y*this.r/r, 2, 0, 2 * Math.PI);
                context.fillStyle = (result) ? '#10ff45' : '#ff343b';
                context.fill();
            },
            // Возвращает реальную координату точки
            getPointCoordinates: function (e) {
                return {
                    x: Number(((e.offsetX - 150) / 28).toFixed(2)),
                    y: Number(((-e.offsetY + 150) / 28).toFixed(2))
                }
            },
            checkArea: function (x, y, r) {
                let pointQuad;
                if (x >= 0 && y >= 0)
                    pointQuad = 1;
                else if (x <= 0 && y >= 0)
                    pointQuad = 2;
                else if (x <= 0 && y <= 0)
                    pointQuad = 3;
                else if (x >= 0 && y <= 0)
                    pointQuad = 4;

                let quad = this.graph[pointQuad];
                r = r * quad['size'];
                switch (quad['figure']) {
                    case 'rect':
                        console.log(x, y, r)
                        return Math.abs(x) <= r && Math.abs(y) <= r;
                    case 'triangle':
                        return Math.abs(x) + Math.abs(y) <= r;
                    case 'arc':
                        return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2);
                    case 'empty':
                        return false;
                }
            },
            drawGraph: function () {
                const plot_canvas = document.querySelector("#plot");
                const context = plot_canvas.getContext("2d");
                let canvLength = plot_canvas.width;

                if (this.rValid) {
                    for (let quad in this.graph) {
                        const plot_canvas = document.querySelector(".quad" + quad);
                        const context = plot_canvas.getContext("2d");
                        let canvLength = plot_canvas.width;
                        context.beginPath();
                        switch (this.graph[quad]['figure']) {
                            case 'rect':
                                this.drawRect(quad, this.graph[quad]['size'], context, canvLength);
                                break;
                            case 'triangle':
                                this.drawTriangle(quad, this.graph[quad]['size'], context, canvLength);
                                break;
                            case 'arc':
                                this.drawArc(quad, this.graph[quad]['size'], context, canvLength);
                                break;
                        }
                        context.closePath();
                        context.fillStyle = '#44acff';
                        context.fill();
                    }
                }

                context.beginPath();
                //Ox
                context.moveTo(10, canvLength / 2);
                context.lineTo(canvLength - 10, canvLength / 2);
                context.lineTo(canvLength - 20, canvLength / 2 - 7);
                context.moveTo(canvLength - 10, canvLength / 2);
                context.lineTo(canvLength - 20, canvLength / 2 + 7);
                //Oy
                context.moveTo(canvLength / 2, canvLength - 10);
                context.lineTo(canvLength / 2, 10);
                context.lineTo(canvLength / 2 - 7, 20);
                context.moveTo(canvLength / 2, 10);
                context.lineTo(canvLength / 2 + 7, 20);
                //Dashes
                for (let i = 10 + 28; i < canvLength * 291 / 300 - 28; i += 28) {
                    context.moveTo(canvLength / 2, canvLength * (i) / 300);
                    context.lineTo(canvLength * 146 / 300, canvLength * (i) / 300);
                    context.lineTo(canvLength * 154 / 300, canvLength * (i) / 300);
                    context.moveTo(canvLength * (i) / 300, canvLength / 2);
                    context.lineTo(canvLength * (i) / 300, canvLength * 146 / 300);
                    context.lineTo(canvLength * (i) / 300, canvLength * 154 / 300);
                }
                context.closePath();
                context.stroke();

                //Names of dashes
                context.fillStyle = '#000000';
                context.font = '14px Arial';
                context.fillText("X", canvLength - 20, canvLength / 2 - 13);
                context.fillText("Y", canvLength / 2 - 20, 20);
                context.fillText("1", canvLength * 173 / 300, canvLength / 2 - canvLength * 10 / 300);
                context.fillText("1", canvLength / 2 - canvLength * 17 / 300, canvLength * 127 / 300);

                //Already drawn points
                for (let point of this.listOfPoints) {
                    this.drawDefinedPoint(point.x, point.y, point.r, point.result);
                }
            },
            drawTriangle: function (quad, size, context, canvLength) {
                let r = this.r * size;
                context.moveTo(0, 0);
                context.lineTo(0, 28 * r);
                context.lineTo(28 * r, 0);
                context.lineTo(0, 0);

            },
            drawRect: function (quad, size, context, canvLength) {
                let r = this.r * size;
                context.rect(canvLength - 28 * r, 0, 28 * r, 28 * r);

            },
            drawArc: function (quad, size, context, canvLength) {
                let r = this.r * size;
                context.arc(0, canvLength, 28 * r, 3*Math.PI / 2, 2*Math.PI);
                context.lineTo(0, canvLength);

            },
            // Затираем все канвасы и рисуем заново
            reDrawGraph: function () {
                const plot_canvas = document.getElementById("plot");
                const context = plot_canvas.getContext('2d');
                context.clearRect(0, 0, plot_canvas.width, plot_canvas.height);
                for (let quad in this.graph) {
                    const plot_canvas = document.querySelector(".quad" + quad);
                    const context = plot_canvas.getContext("2d");
                    let canvLength = plot_canvas.width;
                    context.clearRect(0, 0, canvLength, canvLength);
                }
                this.drawGraph();
            },
            changeButton: function (event) {
                Array.from(event.target.parentElement.children).forEach(el => el.classList.remove('btn-hold'));
                switch (event.target.className) {
                    case 'changeX':
                        this.x = parseInt(event.target.innerText);
                        this.xValid = true;
                        break;
                    case 'changeR':
                        this.r = parseInt(event.target.innerText);
                        this.rValid = this.r > 0;
                        break;
                }
                event.target.classList.add('btn-hold');
            },
            validateY: function () {
                this.yValid = this.y >= -3 && this.y <= 3;
                this.yValid = true;
            },

        },
        mounted() {
            // При загрузке страницы получаем от сервера массив точек и добавляем его в data
            this.getPointsRequest().then(() => {
                this.$nextTick(() => {
                    this.drawGraph();
                });
            });
        }
    }
</script>

<style scoped>
    .content {
        max-width: 1264px;
        margin: 0 auto;
        padding: 0 15px;
    }

    .content-wrapper {
        display: flex;
        flex-wrap: wrap;
        align-items: flex-start;
    }

    .container {
        background-color: #fff;
        border-radius: 5px;
        box-shadow: 0 0 1px 1px hsla(0, 0%, 91%, 1);
    }

    .content-wrapper > *:not(:last-child) {
        margin-right: 20px;
    }

    .content-wrapper > * {
        margin-bottom: 15px;
    }

    .links {
        display: flex;
        justify-content: space-between;
    }

    .alert {
        display: none;
        padding: 10px 18px;
        position: fixed;
        right: 10px;
        bottom: 0;
        background-color: #d13000;
        color: #fff;
        border-top-left-radius: 4px;
        border-top-right-radius: 4px;
        font-size: 0.9rem;
    }

    /* ====== Graph ====== */

    .graph-container {
        position: relative;
    }

    .graph {
        position: relative;
        width: 300px;
        height: 300px;
        background-color: #fff;
        border-radius: 5px;
        z-index: 10;
    }

    #plot {
        cursor: pointer;
    }

    .figures, .quads {
        display: flex;
        flex-wrap: wrap;
        position: absolute;
        top: 0;
        left: 0;
    }

    .quads {
        visibility: hidden;
    }

    .quad {
        width: 150px;
        height: 150px;
        cursor: pointer;
    }

    .quad > span {
        position: relative;
        width: 150px;
        height: 150px;
        cursor: pointer;
    }

    .quad:nth-child(1) > span {
        border-top-left-radius: 5px;
    }

    .quad:nth-child(2) > span {
        border-top-right-radius: 5px;
    }

    .quad:nth-child(3) > span {
        border-bottom-left-radius: 5px;
    }

    .quad:nth-child(4) > span {
        border-bottom-right-radius: 5px;
    }

    #plot {
        position: relative;
    }

    .edit-btn {
        position: absolute;
        top: 7px;
        right: 7px;
    }

    .edit-btn,
    .edit-btn span,
    .quad,
    .quad span {
        display: block;
    }

    .edit-btn:hover .edit-icon,
    .edit-btn input:checked ~ .edit-icon {
        background-color: #f3f3f3;
    }

    .edit-btn input,
    .quad input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
    }

    .edit-icon {
        padding: 2px 5px 4px 9px;
        border-radius: 3px;
        cursor: pointer;
    }

    .edit-icon > span {
        background: url("../assets/edit.svg") no-repeat;
        width: 18px;
        height: 18px;
    }

    .edit-form {
        position: absolute;
        top: 120px;
        z-index: 1;
        width: 280px;
        transition: top 0.5s;
    }

    .edit-form h3 {
        font-size: 1.125rem;
        font-weight: 500;
        margin-bottom: 4px;
    }

    .figure-icon-select span {
        display: block;
    }

    .edit-form-title {
        font-size: 1rem;
        margin-bottom: 4px;
    }

    .edit-form label {
        display: block;
        position: relative;
        cursor: pointer;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    .figure-size-select {
        font-size: 0.875rem;
    }

    .figure-size-select label {
        display: flex;
        align-items: center;
    }

    .figure-size-select label:not(:last-child) {
        margin-right: 15px;
    }

    .edit-form label:hover .checkmark {
        background-color: #ccc;
    }

    .edit-form label input {
        position: absolute;
        top: 0;
        left: 0;
        opacity: 0;
        cursor: pointer;
    }

    .figure-icon-select,
    .figure-size-select {
        display: flex;
    }

    .figure-icon-select:not(:last-child) {
        margin-bottom: 0.4rem;
    }

    .checkmark {
        position: relative;
        display: inline-block;
        height: 16px;
        width: 16px;
        margin-right: 5px;
        background-color: #eee;
        border-radius: 50%;
    }

    .checkmark:after {
        content: "";
        position: absolute;
        display: none;
        top: 4px;
        left: 4px;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background-color: white;
    }

    .edit-form label input:checked ~ .checkmark {
        background-color: #2196F3;
    }

    .edit-form label input:checked ~ .checkmark:after {
        display: block;
    }

    .figure-icon {
        width: 50px;
        padding: 5px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .figure-icon-select > label:not(:last-child) .figure-icon {
        margin-right: 10px;
    }

    .quad span:hover,
    .quad input:checked ~ span,
    .figure-icon:hover,
    .figure-icon-select input:checked ~ .figure-icon {
        background-color: rgba(33, 150, 243, 0.1);
    }

    .figure-icon > span {
        margin: 0 auto;
    }

    .figure-icon-select label input:checked ~ .figure-icon {
        border-color: #2196F3;
    }

    .rect-icon > span {
        width: 30px;
        height: 50px;
        background-color: #44acff;
    }

    .triangle-icon > span {
        width: 0;
        height: 0;
        border-left: 25px solid transparent;
        border-right: 25px solid transparent;
        border-bottom: 50px solid #44acff;
    }

    .arc-icon > span {
        width: 50px;
        height: 50px;
        background-color: #44acff;
        -moz-border-radius: 25px;
        -webkit-border-radius: 25px;
        border-radius: 25px;
    }

    .empty-icon > span {
        width: 50px;
        height: 50px;
    }

    .no-data {
        text-align: center;
        line-height: 2rem;
    }

    .slide-down {
        top: 310px;
    }

    /* ====== Form ====== */

    .selection {
        padding: 10px;
    }

    .form-select {
        margin-bottom: 10px;
    }

    .selection-label {
        display: block;
        margin-bottom: 4px;
        position: relative;
    }

    .selection-label:hover span.visible::after {
        visibility: visible;
        opacity: 1;
    }

    .select-buttons {
    }

    .select-buttons > button {
        display: inline-block;
        width: 1.7rem;
        padding: 4px 0;
        background-color: transparent;
        border: 1px solid #c8c8c8;
        border-right: 0;
        text-align: center;
        outline: none;
        cursor: pointer;
        transition: background-color, color .15s, .15s ease, ease;
    }

    .select-buttons > button:first-child {
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
    }

    .select-buttons > button:last-child {
        border-right: 1px solid #c8c8c8;
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
    }

    .select-buttons > button:hover {
        background-color: #eee;
    }

    .btn-hold {
        background-color: #2196F3 !important;
        color: #fff;
    }

    .form-btn {
        width: 100%;
        padding: 10px 22px;
        border: 0;
        border-radius: 4px;
        color: #fff;
        font-size: 0.9rem;
        cursor: pointer;
        outline: 0;
    }

    .btn-submit {
        background-color: #44acff;
        margin-right: 5px;
    }

    .btn-submit:hover {
        background-color: #44acff;
    }

    .clear-cookie {
        display: none;
        background-color: hsla(17, 83%, 48%, 1);
    }

    .clear-cookie:hover {
        background-color: hsl(17, 83%, 44%);
    }

    .warning {
        content: attr(data-validate);
        display: inline-block;
        visibility: hidden;
        position: relative;
        background-size: 15px;
        width: 15px;
        height: 15px;
        background-image: url("../assets/warning.svg");
        vertical-align: top;
        margin: 2px 0 0 6px;
        transition: opacity 0.5s ease;
        transform-style: preserve-3d;
    }

    .warning::after {
        content: attr(data-validate);
        position: absolute;
        visibility: hidden;
        opacity: 0;
        width: max-content;
        left: -5px;
        top: 50%;
        padding: 4px 4px 3px 23px;
        font-size: 0.8rem;
        font-weight: 500;
        background-color: #fff;
        color: #c80000;
        border: 1px solid #c80000;
        border-radius: 3px;
        transform: translateY(-50%) translateZ(-1px);
        transition: opacity .2s;
    }


</style>

<style>
    #Y-select {
        display: block;
        width: 2rem;
        min-width: 1rem;
        padding: 0 0.5rem;
        border: solid 1px #c8c8c8;
        border-radius: 4px;
    }

    #Y-select:focus {
        border-color: #2086d2;
    }
</style>