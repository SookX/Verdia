import React, { useState } from 'react'
import AccountPage from '@/components/AccountPage'
import AsyncStorage from '@react-native-async-storage/async-storage'
import { crud } from '@/api/crud'
import { useRouter } from 'expo-router'

const Login = () => {
    const router = useRouter()


    const handleSubmit = async () => {
        const response = await crud({
            url: "/auth/login",
            method: "post",
            body: {
                email,
                password
            }
        })

        console.log(JSON.stringify(response))

        if(response?.status == 200) {
            await AsyncStorage.setItem('access', response?.data?.token)
            router.push('/camera')
        }
    }



    // Holds the state for the form
    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")



  return (
    <AccountPage
        title="Welcome back!"
        text="Enter your credentials."
        inputs={[
            {
                label: "Email",
                placeholder: "Email",
                color: "primary-400",
                value: email,
                setValue: setEmail
            },
            {
                label: "Password",
                placeholder: "Password",
                color: "primary-400",
                value: password,
                setValue: setPassword
            }
        ]}
        forgotPass={true}
        buttonLabel="Sign in"
        handleSubmit={handleSubmit}
        link={{
            text: "New to Verdia?",
            label: "Sign up.",
            link: "/register"
        }}
    />
  )
}

export default Login