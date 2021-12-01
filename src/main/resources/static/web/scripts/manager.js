const app = Vue.createApp({
    data(){
        return{
            listClients: [],
            listClient: [],
            listClientID: [],
            listAccounts: [],
            debitCards: [],
            creditCards: [],
            listClientLoans: [],
            clientData:{
                firstName: "",
                lastName: "",
                username: "",
                email: "",
                password: "",
            },
            user:{
                id: "",
                firstName: "",
                lastName: "",
                username: "",
                email: "",
                role: "",
            },
            type: "password",
            hidden: true,
            show: false,
            filterClient: [],
            searchClient: "",
            noClient: false,
            error: false,
            errorMessage: "",
        }
    },
    created(){
        this.loadDataClient()
        this.clients()
        this.listCientID()
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
        numberFormat(data){
            return numeral(data).format("0,0.00")
        },
        addClient(){
            let client = {
                firstName: this.clientData.firstName,
                lastName: this.clientData.lastName,
                username: this.clientData.username,
                email: this.clientData.email,
                password: this.clientData.password
            }
            this.postClient()
        },
        postClient(){
            axios.post("/api/clients",`firstName=${this.clientData.firstName}&lastName=${this.clientData.lastName}&username=${this.clientData.username}&email=${this.clientData.email}&password=${this.clientData.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                swal({
                    title: "Register successful",
                    icon: "success",
                    button: true
                })
                .then(response => {
                    window.location.replace("manager.html")
                })
            })
            .catch(error =>{
                this.error = true
                this.errorMessage = error.response.data
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
        createAccount(idClient){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientID = idClient;

            swal({
                title: "Confirmation",
                text: "Do you want to create a new account?",
                icon: "warning",
                buttons: [true, "Create"]
            })
            .then(confirmation => {
                swal({
                    title: "Select type account",
                    text: "What type account do you want create?",
                    icon: "warning",
                    buttons: {
                        checking: "Checking",
                        savings: "Savings"
                    }
                })
                .then(value => {
                    switch(value){
                        case "checking":
                            swal({
                                title: "Checking account created.",
                                icon: "success",
                                button: true
                            })
                            .then(response => {
                                axios.post(`/api/admin/clients/${this.clientID}/accounts`,`type=Checking`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                
                                window.location.replace("manager.html")
                            })
                        break;
                            
                        case "savings":
                            swal({
                                title: "Savings account created.",
                                icon: "success",
                                button: true
                            })
                            .then(response => {
                                axios.post(`/api/admin/clients/${this.clientID}/accounts`,`type=Savings`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                    
                                window.location.replace("manager.html")
                            })
                    }
                })
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
                            button: true
                        })
                        .then(response =>{
                            window.location.replace("manager.html")
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
                            button: true
                        })
                        .then(response =>{
                            window.location.replace("manager.html")
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
                            button: true
                        })
                        .then(response =>{
                            window.location.replace("manager.html")
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
                            button: true
                        })
                        .then(response =>{
                            window.location.replace("manager.html")
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
        clients(){
            axios.get("/api/admin/clients")
            .then(response =>{
                this.listClients = response.data

                this.user.id = response.data.id
                this.user.firstName = response.data.firstName
                this.user.lastName = response.data.lastName
                this.user.username = response.data.username
                this.user.email = response.data.email
                this.user.role = response.data.role
            })
        },
        listCientID(){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientID = urlParams.get("id");

            axios.get(`/api/admin/clients/${this.clientID}`)
            .then(response =>{
                this.listClientID = response.data
                this.listAccounts = response.data.accounts
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards
                this.listClientLoans = response.data.clientLoans

                this.orderById(this.listAccounts)
                this.orderById(this.debitCards)
                this.orderById(this.creditCards)
                this.orderById(this.listClientLoans)

                this.user.id = response.data.id
                this.user.firstName = response.data.firstName
                this.user.lastName = response.data.lastName
                this.user.username = response.data.username
                this.user.email = response.data.email
                this.user.role = response.data.role
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
        putData(){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/admin/clients/${this.clientID}`,`firstName=${this.user.firstName}&lastName=${this.user.lastName}&username=${this.user.username}&email=${this.user.email}&role=${this.user.role}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
                            icon: "success",
                            button: true
                        })
                        .then(response =>{
                            window.location.replace("manager.html")
                        })
                    })
                }
            })
        },
        deleteUser(){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure delete this client?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/admin/clients/${this.clientID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Client deleted",
                            icon: "success",
                            button: true
                        })
                        .then(response =>{
                            window.location.replace("manager.html")
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
        momentAccount(date){
            return moment(date).format("MMMM DD, YYYY hh:mm a")
        },
        momentCard(date){
            return moment(date).format("MM/YY")
        }
    },
    computed:{
        searchClients(){
            this.filterClient = this.listClients.filter(client => client.email.toLowerCase().includes(this.searchClient.toLowerCase()))
            this.noSearch
            return this.filterClient
        },
        noSearch(){
            if(this.filterClient.length === 0){
               return this.noClient = true
            }else{
                return this.noClient = false
            }
        },
    }
})
app.mount("#app")