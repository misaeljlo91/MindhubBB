const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            listLoans: [],
            listCards: [],
            listAccount: [],
            debitCards: [],
            creditCards: [],
            data:{
                cardColor: "",
                account: ""
            }
        }
    },
    created(){
        this.loadDataClient()
        this.loadDataAccount()
    },
    methods:{
        loadDataClient(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
                this.listAccounts = response.data.accounts
                this.listLoans = response.data.clientLoans
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards
            })
        },
        loadDataAccount(){
            const urlParams = new URLSearchParams(window.location.search);
            this.accountID = urlParams.get("id");

            axios.get(`/api/clients/current/accounts/${this.accountID}`)
            .then(response => {
                this.listAccount = response.data
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
        registerDataCredit(){
            let cards = {
                cardColor: this.data.cardColor,
            }
            this.createCreditCards()
        },
        createCreditCards(){
            swal({
                title: "Confirmation",
                text: "Do you want to create a new credit card?",
                icon: "warning",
                buttons: [true, "Create"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/credit-cards",`color=${this.data.cardColor}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(reponse => {
                        swal({
                            title: "Congratulations!",
                            text: "New credit card created successfully.",
                            icon: "success",
                            button: "View cards"
                        }).then(response =>{
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
        },
        registerDataDebit(){
            let cards = {
                account: this.data.account
            }
            this.createDebitCards()
        },
        createDebitCards(){
            swal({
                title: "Confirmation",
                text: "Do you want to request a new debit card?",
                icon: "warning",
                buttons: [true, "Request"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/debit-cards",`account=${this.data.account}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(reponse => {
                        swal({
                            title: "Congratulations!",
                            text: "New debit card requested successfully.",
                            icon: "success",
                            button: "View accounts"
                        }).then(response =>{
                            window.location.replace("accounts.html")
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
    }
})
app.mount("#app")