const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            listClientLoans: [],
            debittCards: [],
            creditCards: [],
            listCardsAmount: [],
            data:{
                typePay: "",
                number: "",
                date: "",
                cvv: "",
                id: "",
                balance: "",
                amount: ""
            },
            type: "password", hidden: true, show: false
        }
    },
    created(){
        this.loadDataClient()
    },
    methods:{
        loadDataClient(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
                this.listAccounts = response.data.accounts
                this.listClientLoans = response.data.clientLoans
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards

                this.orderById(this.listAccounts)
                this.orderById(this.creditCards)
            })
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
        numberFormat(data){
            return numeral(data).format("0,0.00")
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
        showPassword(){
            if(this.type === "password"){
                this.type = "text"
                this.hidden = false
                this.show = true
            }else{
                this.type = "password"
                this.hidden = true
                this.show = false
            }
        },
        registerDataAccount(){
            let account = {
                number: this.data.number,
                id: this.data.id,
                amount: this.data.amount
            }
            this.payCardAccount()
        },
        payCardAccount(){
            swal({
                title: "Confirmation",
                text: "Do you want pay this credit card?",
                icon: "warning",
                buttons: [true, "Pay"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/pay-card-with-account",{
                        number: this.data.number,
                        id: this.data.id,
                        amount: this.data.amount
                    })
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Pay successfully.",
                            icon: "success",
                            button: true
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
        registerDataDebitCard(){
            let card = {
                number: this.data.number,
                date: this.data.date,
                cvv: this.data.cvv,
                id: this.data.id,
                amount: this.data.amount
            }
            this.payDebitCard()
        },
        payDebitCard(){
            swal({
                title: "Confirmation",
                text: "Do you want pay this card?",
                icon: "warning",
                buttons: [true, "Pay"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/pay-card-with-debit-card",{
                        number: this.data.number,
                        date: this.data.date,
                        cvv: this.data.cvv,
                        id: this.data.id,
                        amount: this.data.amount
                    })
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Pay successfully.",
                            icon: "success",
                            button: true
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
        registerDataCreditCard(){
            let card = {
                number: this.data.number,
                date: this.data.date,
                cvv: this.data.cvv,
                id: this.data.id,
                amount: this.data.amount
            }
            this.payCreditCard()
        },
        payCreditCard(){
            swal({
                title: "Confirmation",
                text: "Do you want pay this card?",
                icon: "warning",
                buttons: [true, "Pay"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/pay-card-with-credit-card",{
                        number: this.data.number,
                        date: this.data.date,
                        cvv: this.data.cvv,
                        id: this.data.id,
                        amount: this.data.amount
                    })
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Pay successfully.",
                            icon: "success",
                            button: true
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
        }
    },
    computed:{
        filterByBalance(){
            this.listCardsAmount = this.creditCards.filter(card => card.balance > 0)
            return this.listCardsAmount
        }
    }
})
app.mount("#app")