import React from 'react'
import AccountPage from '@/components/AccountPage'

const Register = () => {
  return (
    <AccountPage
        title="Welcome!"
        text="Get started with Verdia today."
        inputs={[
            {
                label: "Email",
                placeholder: "Email",
                color: "primary-400"
            },
            {
                label: "Username",
                placeholder: "Username",
                color: "primary-400"
            },
            {
                label: "Password",
                placeholder: "Password",
                color: "primary-400"
            }
        ]}
        forgotPass={false}
        buttonLabel="Sign up"
        link={{
            text: "Already have an account?",
            label: "Sign in.",
            link: "/login"
        }}
    />
  )
}

export default Register