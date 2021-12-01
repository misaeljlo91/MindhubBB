const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            listAccount: [],
            debitCards: [],
            creditCards: [],
            listDirectory: [],
            data:{
                origin: "",
                destination: "",
                holder: "",
                email: "",
                amount: "",
                typ: "",
                description: ""
            },
            accountsDestination: [],
            none: true, select: false, selectContact: false,
            write: false,
            button: false,
            lengthText: 0,
            transfer:{
                date: "",
                reference: "",
                originNumber: "",
                holderDestination: "",
                destinationNumber: "",
                amount: "",
                description: ""
            }
        }
    },
    created(){
        this.loadDataClient()
        this.loadDataAccount()
        this.listContact()
        this.loadDataTransfer()
    },
    methods:{
        loadDataClient(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
                this.listAccounts = response.data.accounts
                this.listLoans = response.data.clientLoans
                this.listDirectory = response.data.directories
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards

                this.orderById(this.listAccounts)
            })
        },
        loadDataAccount(){
            const urlParams = new URLSearchParams(window.location.search);
            this.accountID = urlParams.get("id");

            axios.get(`/api/clients/current/accounts/${this.accountID}`)
            .then(response => {
                this.listAccount = response.data

                this.data.origin = response.data.number
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
        showField(event){
            if(event.target.value === "Client"){
                this.none = false
                this.select = true
                this.selectContact = false
                this.write = false
                this.button = true
            }
            if(event.target.value === "Directory"){
                this.none = false
                this.select = false
                this.selectContact = true
                this.write = false
                this.button = true
            }
            if(event.target.value === "Another"){
                this.none = false
                this.select = false
                this.selectContact = false
                this.write = true
                this.button = false
            }
        },
        transferMoney(){
            let data = {
                origin: this.data.origin,
                destination: this.data.destination,
                amount: this.data.amount,
                description: this.data.description
            }
            this.transferFrom()
        },
        transferFrom(){
            swal({
                title: "Confirmation",
                text: "Do you want transfer to this account?",
                icon: "warning",
                buttons: [true, "Transfer"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/transactions",`origin=${this.data.origin}&destination=${this.data.destination}&amount=${this.data.amount}&description=${this.data.description}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Transfer successfully sent.",
                            icon: "success",
                            button: true
                        })
                        .then(response => {
                            window.location.replace("accounts.html")
                        })
                    })
                    .catch(erros => {
                        swal({
                            text: erros.response.data,
                            icon: "error"
                        })
                    })
                }
            })
        },
        transferAndAdd(){
            let data = {
                origin: this.data.origin,
                destination: this.data.destination,
                holder: this.data.holder,
                email: this.data.email,
                amount: this.data.amount,
                description: this.data.description
            }
            this.transferFromAdd()
        },
        transferFromAdd(){
            swal({
                title: "Confirmation",
                text: "Do you want transfer to this account?",
                icon: "warning",
                buttons: [true, "Transfer"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.post("/api/clients/current/transactions",`origin=${this.data.origin}&destination=${this.data.destination}&amount=${this.data.amount}&description=${this.data.description}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response=>{
                        swal({
                            title: "Great!",
                            text: "Transfer successfully sent.",
                            icon: "success",
                            button: true
                        })
                        .then(response => {
                            swal({
                                title: "Confirmation",
                                text: "Do you want to add this account to your directory?",
                                icon: "warning",
                                buttons: ["No", "Add"]
                            })
                            .then(confirmation =>{
                                if(confirmation){
                                    axios.post("/api/clients/current/directories",`holder=${this.data.holder}&account=${this.data.destination}&email=${this.data.email}&observation=${this.data.description}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                    .then(response => {
                                        swal({
                                            title: "Register successful",
                                            icon: "success",
                                            button: true
                                        })
                                        .then(response => {
                                            window.location.replace("accounts.html")
                                        })
                                    })
                                    .catch(error =>{
                                        swal({
                                            text: error.response.data,
                                            icon: "error"
                                        })
                                        .then(response => {
                                            window.location.replace("accounts.html")
                                        })
                                    })
                                }else{
                                    window.location.replace("accounts.html")
                                }
                            })
                        })
                    })
                    .catch(error => {
                        swal({
                            text: error.response.data,
                            icon: "error",
                            button: true
                        })
                        .then(confirmation => {
                            window.location.replace("accounts.html")
                        })
                    })
                }
            })
        },
        listContact(){
            const urlParams = new URLSearchParams(window.location.search);
            this.contactID = urlParams.get("id");

            axios.get(`/api/clients/current/directories/${this.contactID}`)
            .then(response =>{
                this.listDirectoryID = response.data

                this.data.destination = response.data.accountNumber
            })
        },
        loadDataTransfer(){
            const urlParams = new URLSearchParams(window.location.search);
            this.transferID = urlParams.get("id");

            axios.get(`/api/clients/current/transactions/${this.transferID}`)
            .then(response => {
                this.transfer.date = response.data.creationDate
                this.transfer.reference = response.data.numberDescription
                this.transfer.originNumber = response.data.account1
                this.transfer.holderDestination = response.data.holder2
                this.transfer.destinationNumber = response.data.account2
                this.transfer.amount = response.data.amount
                this.transfer.type = response.data.type
                this.transfer.description = response.data.description
            })
        },
        momentTransfer(date){
            return moment(date).format("DD/MMM/YY - HH:mm")
        },
    },
    computed:{
        filterDestination(){
            this.accountsDestination = this.listAccounts.filter(account => account.number != this.data.origin)
            return this.accountsDestination
        },
        counter(){
            return this.lengthText + this.data.description.length
        }
    }
})
app.mount("#app")