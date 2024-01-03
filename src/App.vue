<template>
<div id="app">
    <div class="header">
        <div class="header-inner">
            <div>Авшистер Ольга P3213</div>
            <div>Вариант 2341</div>
        </div>
    </div>
    <router-view/>
    <div class="alert"></div>
</div>
</template>

<script>
    export default {
        name: "App"
    }
</script>

<style>
    @import url('https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,900&display=swap&subset=cyrillic');

    :focus {outline: none;}

    * {
        margin: 0;
        padding: 0;
    }

    html {
        font-size: 16px;
    }

    body {
        background-color: #f7f7f7;
        font-family: 'Roboto', Arial, sans-serif;
        overflow-x: hidden;
        color: #2c3e50;
    }

    table {
        border-spacing: 0;
        border-collapse: collapse;
        table-layout: fixed;
    }

    h1 {
        margin-bottom: 6px;
        font-size: 1.6rem;
        font-weight: 500;
    }

    a, router-link {
        color: #2c3e50;
        text-decoration: none;
    }

    h2 {
        margin-bottom: 15px;
        font-weight: 500;
    }

    input {
        font-family: 'Roboto', Arial, sans-serif;
        outline: 0;
    }

    button {
        background-color: transparent;
        border: 0;
    }

    .header {
        width: 100%;
        margin-bottom: 20px;
        background-color: #fafafb;
        font-size: 1rem;
        box-shadow: 0 1px 0 rgba(12,13,14,0.1), 0 1px 6px rgba(59,64,69,0.1);
        color: #212223;
        border-top: 3px solid #00ccbe;
    }

    .header-inner {
        display: flex;
        max-width: 1264px;
        height: 46px;
        margin: 0 auto;
        padding: 0 15px;
        align-items: center;
        font-size: 15px;
        font-weight: 300;
    }

    .header-inner > * {
        width: calc(100% / 3);
    }

    /*.header-inner > *:first-child {
        text-align: left;
    }

    .header-inner > *:last-child {
        text-align: right;
    }*/

    .header-logo {
        padding: 5px 0;
        margin: 0 15px;
        min-width: 204px;
    }

    .header-logo img {
        display: block;
        height: 36px;
        margin: 0 auto;
    }

    .form-wrapper {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        left: 0;
        display: flex;
        align-items: center;
        align-content: center;
        justify-content: center;
        overflow: auto;
    }

    .clearfix::after {
        content: "";
        display: table;
        clear: both;
    }

    .green {
        color: #14ce81;
    }

    .red {
        color: #E04E15;
    }

    .text-center {
        text-align: center;
    }

    .text-gray {
        color: #949494;
    }

    .visible {
        opacity: 1 !important;
        visibility: visible !important;
    }

    @media (max-width: 500px) {
        .header-inner > * {
            width: calc(100% / 2);
        }

        .header-logo {
            display: none;
        }
    }
</style>
