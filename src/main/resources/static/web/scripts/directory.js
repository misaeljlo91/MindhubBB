const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            debitCards: [],
            creditCards: [],
            listDirectory: [],
            listDirectoryID: [],
            filterContact: [],
            noContact: false,
            searchContact: "",
            lengthText: 0,
            data:{
                holder: "",
                account: "",
                email: "",
                observation: ""
            },
            contact:{
                holder: "",
                account: "",
                email: "",
                observation: ""
            },
            show: false,
            error: false,
            errorMessage: ""
        }
    },
    created(){
        this.loadDataClient()
        this.listContact()
    },
    methods:{
        loadDataClient(){
            axios.get("/api/clients/current")
            .then(response => {
                this.listClient = response.data
                this.listAccounts = response.data.accounts
                this.debitCards = response.data.cards
                this.creditCards = response.data.creditCards
                this.listDirectory = response.data.directories

                this.orderById(this.listDirectory)
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
                    return -1
                }else if(a.id > b.id){
                    return 1
                }else{
                    return 0
                }
            })
        },
        addContact(){
            let contact = {
                holder: this.data.holder,
                account: this.data.account,
                email: this.data.email,
                observation: this.data.observation
            }
            this.postContact()
        },
        postContact(){
            swal({
                title: "Confirmation",
                text: "Do you want to add this account?",
                icon: "warning",
                buttons: [true, "Add"]
            })
            .then(confirmation =>{
                if(confirmation){
                    axios.post("/api/clients/current/directories",`holder=${this.data.holder}&account=${this.data.account}&email=${this.data.email}&observation=${this.data.observation}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            title: "Register successful",
                            icon: "success",
                            button: true
                        })
                        .then(response => {
                            window.location.replace("directory.html")
                        })
                    })
                    .catch(error =>{
                        swal({
                            text: error.response.data,
                            icon: "error"
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

                this.contact.holder = response.data.accountHolder
                this.contact.account = response.data.accountNumber
                this.contact.email = response.data.email
                this.contact.observation = response.data.observation
            })
        },
        putData(){
            const urlParams = new URLSearchParams(window.location.search);
            this.contactID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/current/directories/${this.contactID}`,`holder=${this.contact.holder}&account=${this.contact.account}&email=${this.contact.email}&observation=${this.contact.observation}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("directory.html")
                        })
                    })
                    .catch(error =>{
                        swal({
                            text: error.response.data,
                            icon: "error"
                        })
                    })
                }
            })
        },
        deleteData(){
            const urlParams = new URLSearchParams(window.location.search);
            this.contactID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure delete the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/clients/current/directories/${this.contactID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("directory.html")
                        })
                    })
                }
            })
        }
    },
    computed:{
        searchContacts(){
            this.filterContact = this.listDirectory.filter(contact => contact.accountHolder.toLowerCase().includes(this.searchContact.toLowerCase()))

            this.filterContact = this.listDirectory.filter(contact => contact.accountNumber.toLowerCase().includes(this.searchContact.toLowerCase()))

            this.noSearch
            return this.filterContact
        },
        noSearch(){
            if(this.filterContact.length === 0){
               return this.noContact = true
            }else{
                return this.noContact = false
            }
        },
        counter(){
            return this.lengthText + this.data.observation.length
        }
    }
})
app.mount("#app")