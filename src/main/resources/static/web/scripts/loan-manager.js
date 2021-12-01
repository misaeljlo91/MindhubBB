const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listLoans: [],
            listLoanID: [],
            data:{
                name: "",
                amount: "",
                average: "",
                payments: ""
            },
            payments:{
                payment1: "", payment2: "", payment3: ""
            }
        }
    },
    created(){
        this.loadDataClient()
        this.loans()
        this.listLoansID()
    },
    methods:{
        loadDataClient(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
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
                        window.location.replace("index.html")
                    })
                }
            })
        },
        addLoan(){
            let Loan = {
                name: this.data.name,
                amount: this.data.amount,
                average: this.data.average,
                payment1: this.payments.payment1,
                payment2: this.payments.payment2,
                payment3: this.payments.payment3
            }
            this.postLoan()
        },
        postLoan(){
            axios.post("/api/admin/loans",`name=${this.data.name}&maxAmount=${this.data.amount}&averageInterest=${this.data.average}&payment1=${this.payments.payment1}&payment2=${this.payments.payment2}&payment3=${this.payments.payment3}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                swal({
                    title: "Register successful",
                    icon: "success",
                    button: true
                })
                .then(response => {
                    window.location.replace("loan-manager.html")
                })
            })
            .catch(error =>{
                this.error = true
                this.errorMessage = error.response.data
            })
        },
        loans(){
            axios.get("/api/admin/loans")
            .then(response =>{
                this.listLoans = response.data

                this.data.name = response.data.name
                this.data.amount = response.data.maxAmount
                this.data.average = response.data.averageInterest
                this.data.payments = response.data.payments
            })
        },
        listLoansID(){
            const urlParams = new URLSearchParams(window.location.search);
            this.loanID = urlParams.get("id");

            axios.get(`/api/admin/loans/${this.loanID}`)
            .then(response =>{
                this.listLoanID = response.data

                this.data.name = response.data.name
                this.data.amount = response.data.maxAmount
                this.data.average = response.data.averageInterest
                this.data.payments = response.data.payments
            })
        },
        putData(){
            const urlParams = new URLSearchParams(window.location.search);
            this.loanID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/admin/loans/${this.loanID}`,`name=${this.data.name}&maxAmount=${this.data.amount}&averageInterest=${this.data.average}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("loan-manager.html")
                        })
                    })
                }
            })
        },
        deleteLoan(){
            const urlParams = new URLSearchParams(window.location.search);
            this.loanID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure delete this loan?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/admin/loans/${this.loanID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Loan deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("loan-manager.html")
                        })
                    })
                }
            })
        }
    }
})
app.mount("#app")