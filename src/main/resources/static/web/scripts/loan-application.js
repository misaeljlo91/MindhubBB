const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            debitCards: [],
            creditCards: [],
            listClientLoans: [],
            listAccount: [],
            listLoans: [],
            listLoan: [],
            ListPayment: [],
            data:{
                name: "",
                payments: "",
                amount: "",
                averageInterest: "",
                account: ""
            },
            writeData: true, validationData: false
        }
    },
    created(){
        this.loadDataClient()
        this.loadDataLoans()
    },
    methods:{
        loadDataClient(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
                this.listAccounts = response.data.accounts
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards
                this.listClientLoans = response.data.clientLoans

                this.creditCards = this.listCards.filter(card => card.type === "Credit")
            })
        },
        loadDataLoans(){
            axios.get("/api/clients/current/loans")
            .then(response => {
                this.listLoans = response.data
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
        showValidation(){
            this.writeData = false
            this.validationData = true
        },
        showForm(){
            this.writeData = true
            this.validationData = false
        },
        registerData(){
            let data = {
                name: this.data.name,
                payments: this.data.payments,
                amount: this.data.amount,
                account: this.data.account
            }
            this.requestLoans()
        },
        requestLoans(){
            swal({
                title: "Confirmation",
                text: "Do you want to request this loan?",
                icon: "warning",
                buttons: [true, "Create"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/loans",{
                        name: this.data.name,
                        payments: this.data.payments,
                        amount: this.data.amount,
                        account: this.data.account
                    })
                    .then(response => {
                        swal({
                            title: "Congratulations!",
                            text: "Loan approved.",
                            icon: "success",
                            button: "Continue"
                        })
                        .then(response =>{
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
    },
    computed:{
        filterLoans(){
            this.listLoan = this.listLoans.filter(loan => loan.name == this.data.name)
            return this.listLoan
        },
        filterPayments(){
            this.ListPayment = this.filterLoans.map(loan => loan.payments)
            return this.ListPayment
        }
    }
})
const consola = app.mount("#app")