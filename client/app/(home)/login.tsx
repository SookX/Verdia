import React from 'react'
import AccountPage from '@/components/AccountPage'

const Login = () => {
  return (
    <AccountPage
        title="Welcome back!"
        text="Enter your credentials."
        inputs={[
            {
                label: "Email",
                placeholder: "Email",
                color: "primary-400"
            },
            {
                label: "Password",
                placeholder: "Password",
                color: "primary-400"
            }
        ]}
        forgotPass={true}
        buttonLabel="Sign in"
        link={{
            text: "New to Verdia?",
            label: "Sign up.",
            link: "/register"
        }}
    />
  )
}

export default Login