
const updateSW = registerSW({
    onNeedRefresh() {
        // Show a prompt to the user asking them to refresh
        console.log('New content available, click on reload button to update.')
    },
    onOfflineReady() {
        console.log('App is ready to work offline.')
    },
})
