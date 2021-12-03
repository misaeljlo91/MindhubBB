const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            listCard: [],
            debitCards: [],
            creditCards: [],
            dateNow: "",
            show: false, hidden: true,
            showCVV: false, hiddenCVV: true
        }
    },
    created(){
        this.loadData()
        this.loadDataCard()
    },
    methods:{
        loadData(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
                this.listAccounts = response.data.accounts
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards

                this.orderById(this.debitCards)
                this.orderById(this.creditCards)

                let date = new Date();
                let month = date.getMonth() + 1;
                let year = date.getFullYear().toString().slice(2);

                this.dateNow = month+"/"+year
            })
        },
        loadDataCard(){
            const urlParams = new URLSearchParams(window.location.search);
            this.cardID = urlParams.get("id");

            axios.get(`/api/clients/current/cards/${this.cardID}`)
            .then(response => {
                this.listCard = response.data
            })
        },
        momentCard(date){
            return moment(date).format("MM/YY")
        },
        logOut(){
            swal({
                text: "Are you sure sign out?",
                icon:"warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/logout")
                    .then(response=>{
                        window.location.replace("../index.html")
                    })
                }
            })
        },
        orderById(array){
            return array.sort((a, b) => {
                if(a.id < b.id){
                    return -1
                }else if(a.id > b.id){
                    return 1
                }else{
                    return 0
                }
            })
        },
        showNumber(id){
            this.show = true
            this.hidden = false
        },
        hiddenNumber(id){
            this.show = false
            this.hidden = true
        },
        showBack(){
            this.hiddenCVV = false
            this.showCVV = true
        },
        showFront(){
            this.hiddenCVV = true
            this.showCVV = false
        },
        renovateDebitCard(id){
            const urlParams = new URLSearchParams(window.location.search);
            this.cardID = id;

            swal({
                title: "Confirmation",
                text: "Are you sure want renovate this debit card?",
                icon: "warning",
                buttons: [true, "Continue"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/current/debit-cards/${this.cardID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response=> {
                        swal({
                            title: "Great!",
                            text: "Debit card renovated",
                            icon: "success"
                        })
                        .then(response => {
                            window.location.replace("debit-cards.html")
                        })
                    })
                    .catch(error => {
                        swal({
                            title: "Attention!",
                            text: error.response.data,
                            icon: "error"
                        })
                    })
                }
            })
        },
        deleteDebitCard(id){
            const urlParams = new URLSearchParams(window.location.search);
            this.cardID = id;

            swal({
                title: "Confirmation",
                text: "Are you sure delete this debit card?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/clients/current/debit-cards/${this.cardID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Debit card deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("debit-cards.html")
                        })
                    })
                }
            })
        },
        renovateCreditCard(id){
            const urlParams = new URLSearchParams(window.location.search);
            this.cardID = id;

            
            swal({
                title: "Confirmation",
                text: "Are you sure want renovate this credit card?",
                icon: "warning",
                buttons: [true, "Continue"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/current/credit-cards/${this.cardID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response=> {
                        swal({
                            title: "Great!",
                            text: "Credit card renovated",
                            icon: "success"
                        })
                        .then(response => {
                            window.location.replace("credit-cards.html")
                        })
                    })
                    .catch(error => {
                        swal({
                            title: "Attention!",
                            text: error.response.data,
                            icon: "error"
                        })
                    })
                }
            })
        },
        deleteCreditCard(id){
            const urlParams = new URLSearchParams(window.location.search);
            this.cardID = id;

            swal({
                title: "Confirmation",
                text: "Are you sure delete this credit card?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/clients/current/credit-cards/${this.cardID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Credit card deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("credit-cards.html")
                        })
                    })
                    .catch(error => {
                        swal({
                            text: error.response.data,
                            icon: "error"
                        })
                    })
                }
            })
        }
    },
})
app.mount("#app")