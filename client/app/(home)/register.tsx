import React, { useState } from 'react'
import AccountPage from '@/components/AccountPage'
import { crud } from '@/api/crud'
import axios from 'axios'

const Register = () => {
    const handleSubmit = async () => {
        // const response = await crud({
        //     url: "/auth/register",
        //     method: "post",
        //     body: {
        //         email,
        //         username,
        //         password
        //     }
        // })
        console.log("Vlizma li")

        try {
            const response = await axios.post('http://localhost:8080/api/auth/register', {
                email,
                username,
                password
            })
            console.log(JSON.parse(JSON.stringify(response)))
        } catch(err) {
            console.log(JSON.parse(JSON.stringify(err)))
        }

    }



    // Holds the state for the form
    const [email, setEmail] = useState<string>("")
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")



    return (
        <AccountPage
            title="Welcome!"
            text="Get started with Verdia today."
            inputs={[
                {
                    label: "Email",
                    placeholder: "Email",
                    color: "primary-400",
                    value: email,
                    setValue: setEmail,
                },
                {
                    label: "Username",
                    placeholder: "Username",
                    color: "primary-400",
                    value: username,
                    setValue: setUsername,
                },
                {
                    label: "Password",
                    placeholder: "Password",
                    color: "primary-400",
                    value: password,
                    setValue: setPassword,
                }
            ]}
            forgotPass={false}
            buttonLabel="Sign up"
            handleSubmit={handleSubmit}
            link={{
                text: "Already have an account?",
                label: "Sign in.",
                link: "/login"
            }}
        />
    )
}

export default Register