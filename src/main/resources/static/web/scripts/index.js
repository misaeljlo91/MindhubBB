const app = Vue.createApp({
    data(){
        return{
            sign:{
                username: "",
                password: "",
            },
            user:{
                firstName: null,
                lastName: null,
                username: null,
                email: null,
                password: null,
            },
            data:{
                email: "",
                username: "",
                password: "",
                password2: ""
            },
            type: "password", hidden: true, show: false,
            type2: "password", hidden2: true, show2: false,
            type3: "password", hidden3: true, show3: false,
            type4: "password", hidden4: true, show4: false,
            error: false
        }
    },
    methods:{
        signIn(){
            axios.post("/api/login",`username=${this.sign.username}&password=${this.sign.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                window.location.replace("webPages/persons.html")
            })
            .catch(error => {
                this.error = true
                this.sign.username = ""
                this.sign.password = ""
            })
        },
        registerClient(){
            let newUser = {
                firstName: this.user.firstName,
                lastName: this.user.lastName,
                username: this.user.username,
                email: this.user.email,
                password: this.user.password,
            }
            this.postClient()
        },
        postClient(){
            axios.post("/api/clients",`firstName=${this.user.firstName}&lastName=${this.user.lastName}&username=${this.user.username}&email=${this.user.email}&password=${this.user.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                swal({
                    title: "Register successful",
                    icon: "success",
                    button: true
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
                                    text: "Remember that this account has associated one debit card.",
                                    icon: "success",
                                    button: true
                                })
                                .then(response => {
                                    axios.post("/api/login",`firstName=${this.user.firstName}&lastName=${this.user.lastName}&username=${this.user.username}&email=${this.user.email}&password=${this.user.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                    .then(response => {
                                        axios.post("/api/clients/current/accounts",`type=Checking`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                        
                                        window.location.replace("webPages/persons.html")
                                    })
                                })
                            break;
                            
                            case "savings":
                                swal({
                                    title: "Savings account created.",
                                    text: "Remember that this account has associated one debit card.",
                                    icon: "success",
                                    button: true
                                })
                                .then(response => {
                                    axios.post("/api/login",`firstName=${this.user.firstName}&lastName=${this.user.lastName}&username=${this.user.username}&email=${this.user.email}&password=${this.user.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                    .then(response => {
                                        axios.post("/api/clients/current/accounts",`type=Savings`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                                        
                                        window.location.replace("webPages/persons.html")
                                    })
                                })
                        }
                    })
                })
            })
            .catch(error => {
                swal({
                    text: error.response.data,
                    icon: "error"
                })
            })
        },
        changeUsername(){
            swal({
                title: "Confirmation",
                text: "Are you sure to change the username?",
                icon: "warning",
                buttons: [true, "Continue"]
            })
            .then(confirmation => {
                if(confirmation){
                    axios.put(`/api/clients/username`,`email=${this.data.email}&username=${this.data.username}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data changed",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("index.html")
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
        changePassword(){
            swal({
                title: "Confirmation",
                text: "Are you sure to change the password?",
                icon: "warning",
                buttons: [true, "Continue"]
            })
            .then(confirmation => {
                if(this.data.password == this.data.password2){
                    axios.put(`/api/clients/password`,`email=${this.data.email}&password=${this.data.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        swal({
                            text: "Data changed",
                            icon: "success",
                            button: "Back"
                        })
                        .then(response =>{
                            window.location.replace("index.html")
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
        showPassword2(){
            if(this.type2 === "password"){
                this.type2 = "text"
                this.hidden2 = false
                this.show2 = true
            }else{
                this.type2 = "password"
                this.hidden2 = true
                this.show2 = false
            }
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
        }
    },
})
app.mount("#app")