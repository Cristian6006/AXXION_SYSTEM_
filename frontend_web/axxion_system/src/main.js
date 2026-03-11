import { createApp } from 'vue'
import { registerSW } from 'virtual:pwa-register'
import App from './App.vue'
import router from './router'
import './Style.css'
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import ECharts from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, PieChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'



/* import all the icons in Free Solid, Free Regular, and Brands styles */
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import axios from '@/plugins/axios';
import { createPinia } from 'pinia';
library.add(fas, far, fab)

use([CanvasRenderer, BarChart, PieChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])


const app = createApp(App)
const pinia = createPinia()
app.component('v-chart', ECharts)
app.component('font-awesome-icon', FontAwesomeIcon)
app.use(pinia)
app.use(router)
app.mount('#app')
const updateSW = registerSW({
    onNeedRefresh() {
        console.log('New content available, click on reload button to update.')
    },
    onOfflineReady() {
        console.log('App is ready to work offline.')
    },
})




