const app = Vue.createApp({
    data(){
        return{
            listClient: [],
            listAccounts: [],
            debitCards: [],
            creditCards: [],
            clientPassword: "",
            user:{
                firstName: null,
                lastName: null,
                email: null,
                password: null,
                password2: null,
                profile: null
            },
            data:{
                username: "",
                password: ""
            },
            type3: "password", hidden3: true, show3: false,
            type4: "password", hidden4: true, show4: false,
            error: false
        }
    },
    created(){
        this.loadDataClient()
        this.loadData()
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
        loadData(){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientID = urlParams.get("id");

            axios.get(`/api/clients/current/${this.clientID}`)
            .then(response => {
                this.user.firstName = response.data.firstName
                this.user.lastName = response.data.lastName
                this.user.email = response.data.email

                this.data.username = response.data.username

                this.clientPassword = response.data.password
            })
        },
        putClient(){
            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/current/personal`,`firstName=${this.user.firstName}&lastName=${this.user.lastName}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("persons.html")
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
        logOut(){
            swal({
                text: "Are you sure sign out?",
                icon: "warning",
                buttons: true
            })
            .then((confirmation) => {
                if(confirmation){
                    axios.post("/api/logout")
                    .then(response=>{
                        window.location.replace("../index.html")
                    })
                }
            })
        },
        putPassword(){
            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(this.user.password == this.user.password2){
                    axios.put(`/api/clients/current/password`,`password=${this.user.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("persons.html")
                        })
                    })
                    .catch(error => {
                        swal({
                            text: error.response.data,
                            icon: "error"
                        })
                    })    
                }else{
                    swal({
                        title: "Attention!",
                        text: "Passwords not equals.",
                        icon: "error"
                    })
                }
            })
        },
        putUsername(){
            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/current/username`,`username=${this.data.username}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
                            icon: "success",
                            button: "Accept"
                        })
                        .then(response =>{
                            window.location.replace("../index.html")
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
        putProfile(){
            swal({
                title: "Confirmation",
                text: "Are you sure save the data?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/current/profile`,`profile=${this.user.profile}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data saved",
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
        deleteUser(){
            const urlParams = new URLSearchParams(window.location.search);
            this.clientID = urlParams.get("id");

            swal({
                title: "Confirmation",
                text: "Are you sure delete your user?",
                icon: "warning",
                buttons: true
            })
            .then(confirmation => {
                if(confirmation){
                    axios.delete(`/api/clients/current/${this.clientID}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "User deleted",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("../index.html")
                        })
                    })
                    .catch(error => {
                        swal({
                            title: "Attention!",
                            text: error.response.data,
                            icon: "error",
                            buttons: [true, "Continue"]
                        })
                        .then(response => {
                            window.location.replace("persons.html")
                        })
                    })
                }
            })
        },
        showPassword3(){
            if(this.type3 === "password"){
                this.type3 = "text"
                this.hidden3 = false
                this.show3 = true
            }else{
                this.type3 = "password"
                this.hidden3 = true
                this.show3 = false
            }
        },
        showPassword4(){
            if(this.type4 === "password"){
                this.type4 = "text"
                this.hidden4 = false
                this.show4 = true
            }else{
                this.type4 = "password"
                this.hidden4 = true
                this.show4 = false
            }
        },
        showAlert(){
            this.error = true
        }
    }
})
app.mount("#app")