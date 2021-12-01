const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            listLoans: [],
            debitCards: [],
            creditCards: [],
            data:{
                type: ""
            },
            dollar: [],
            dateDollar: "",
            filterLoans: [],
            noLoans: false,
            searchLoan: "",
        }
    },
    created(){
        this.loadDataClient()
        this.loadDollar()
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

                this.orderById(this.listAccounts)
                this.orderById(this.listLoans)
                this.orderById(this.debitCards)
                this.orderById(this.creditCards)

                this.searchLoans()
            })
        },
        loadDollar(){
            axios.get("https://www.dolarsi.com/api/api.php?type=valoresprincipales")
            .then(response => {
                this.dollar = response.data[0].casa
                this.dateDollar = moment().format("DD/MM/YYYY")
            })
        },
        momentAccount(date){
            return moment(date).format("MMMM DD, YYYY hh:mm a")
        },
        momentCard(date){
            return moment(date).format("MM/YY")
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
                    return -1
                }else if(a.id > b.id){
                    return 1
                }else{
                    return 0
                }
            })
        },
        registerData(){
            let accounts = {
                type: this.data.type
            }
            this.createAccounts()
        },
        createAccounts(){
            swal({
                title: "Confirmation",
                text: "Do you want to create a new account?",
                icon: "warning",
                buttons: [true, "Create"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/accounts",`type=${this.data.type}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response=>{
                        swal({
                            title: "Congratulations!",
                            text: "New account created successfully.",
                            icon:"success",
                            button: true
                        })
                        .then(response => {
                            window.location.replace("accounts.html")
                        })
                    })
                    .catch(error => {
                        swal({
                            title: "Attention!",
                            text: error.response.data,
                            icon: "error",
                            button: true
                        })
                        .then(response => {
                            window.location.replace("debit-cards.html")
                        })
                    })
                }
            })
        },
        deleteAccount(id){
            const urlParams = new URLSearchParams(window.location.search);
            this.accountID = id;

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
        },
        deleteLoan(id){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientLoanID = id;

            swal({
                title: "Confirmation",
                text: "Are you sure delete this loan?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/clients/current/clientloans/${this.clientLoanID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Loan deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("loans.html")
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
        renovateDebitCard(id){
            const documentURL = window.location.pathname.split("/").pop();

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
                            window.location.replace("persons.html")
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
                            window.location.replace("persons.html")
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
                            window.location.replace("persons.html")
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
    }
})
app.mount("#app")