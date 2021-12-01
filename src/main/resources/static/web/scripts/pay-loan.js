const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            listClientLoans: [],
            debitCards: [],
            creditCards: [],
            listLoanAmount: [],
            listLoan: [],
            data:{
                typePay: "",
                number: "",
                cardholder: "",
                date: "",
                cvv: "",
                id: "",
                name: "",
                payments: "",
                monthlyPayment: ""
            },
            type: "password", hidden: true, show: false,
            writeData: true, validationData: false
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
                this.orderById(this.listClientLoans)
            })
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
        showValidation(){
            this.writeData = false
            this.validationData = true
        },
        showForm(){
            this.writeData = true
            this.validationData = false
        },
        registerDataAccount(){
            let account = {
                number: this.data.number,
                id: this.data.id,
                name: this.data.name,
                payments: this.data.payments,
                monthlyPayment: this.data.monthlyPayment
            }
            this.payLoanAccount()
        },
        payLoanAccount(){
            swal({
                title: "Confirmation",
                text: "Do you want pay this loan?",
                icon: "warning",
                buttons: [true, "Pay"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/pay-with-account",{
                        number: this.data.number,
                        id: this.data.id,
                        name: this.data.name,
                        payments: this.data.payments,
                        monthlyPayment: this.data.monthlyPayment
                    })
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Pay successfully.",
                            icon: "success",
                            button: true
                        })
                        .then(response => {
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
        registerDataDebitCard(){
            let card = {
                number: this.data.number,
                date: this.data.date,
                cvv: this.data.cvv,
                id: this.data.id,
                name: this.data.name,
                payments: this.data.payments,
                monthlyPayment: this.data.monthlyPayment
            }
            this.payLoanDebitCard()
        },
        payLoanDebitCard(){
            swal({
                title: "Confirmation",
                text: "Do you want pay this loan?",
                icon: "warning",
                buttons: [true, "Pay"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/pay-with-debit-card",{
                        number: this.data.number,
                        date: this.data.date,
                        cvv: this.data.cvv,
                        id: this.data.id,
                        name: this.data.name,
                        payments: this.data.payments,
                        monthlyPayment: this.data.monthlyPayment
                    })
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Pay successfully.",
                            icon: "success",
                            button: true
                        })
                        .then(response => {
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
        registerDataCreditCard(){
            let card = {
                number: this.data.number,
                date: this.data.date,
                cvv: this.data.cvv,
                id: this.data.id,
                name: this.data.name,
                payments: this.data.payments,
                monthlyPayment: this.data.monthlyPayment
            }
            this.payLoanCreditCard()
        },
        payLoanCreditCard(){
            swal({
                title: "Confirmation",
                text: "Do you want pay this loan?",
                icon: "warning",
                buttons: [true, "Pay"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/pay-with-credit-card",{
                        number: this.data.number,
                        date: this.data.date,
                        cvv: this.data.cvv,
                        id: this.data.id,
                        name: this.data.name,
                        payments: this.data.payments,
                        monthlyPayment: this.data.monthlyPayment
                    })
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Pay successfully.",
                            icon: "success",
                            button: true
                        })
                        .then(response => {
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
        }
    },
    computed:{
        filterByAmount(){
            this.listLoanAmount = this.listClientLoans.filter(loan => loan.amount > 0)
            return this.listLoanAmount
        },
        filterLoans(){
            this.listLoan = this.filterByAmount.filter(loan => loan.id == this.data.id)
            return this.listLoan
        },
    }
})
app.mount("#app")