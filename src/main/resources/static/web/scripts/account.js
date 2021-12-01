const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            debitCards: [],
            creditCards: [],
            listAccount: [],
            listTransactions: [],
            filterDescription: [],
            noTransaction: false,
            searchTransaction: "",
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
                this.listTransactions = response.data.transactions

                this.orderById(this.listTransactions)
                this.searchTransactions()
            })
        },
        momentTransaction(date){
            return moment(date).format("DD/MMM/YYYY")
        },
        numberFormat(data){
            return numeral(data).format("0,0.00")
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
                    return 1
                }else if(a.id > b.id){
                    return -1
                }else{
                    return 0
                }
            })
        },
        deleteAccount(){
            const urlParams = new URLSearchParams(window.location.search);
            this.accountID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure delete this account?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/clients/current/accounts/${this.accountID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Account deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("accounts.html")
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
        searchTransactions(){
            this.filterDescription = this.listTransactions.filter(transaction => transaction.numberDescription.toLowerCase().includes(this.searchTransaction.toLowerCase()))
            this.noSearch
            return this.filterDescription
        },
        noSearch(){
            if(this.filterDescription.length === 0){
               return this.noTransaction = true
            }else{
                return this.noTransaction = false
            }
        }
    }
})
app.mount("#app")