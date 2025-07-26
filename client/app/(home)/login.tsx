import { Image, StyleSheet, Text, View, ImageBackground, TextInput, TouchableOpacity } from 'react-native'
import React from 'react'
import { images } from '@/constants/images'
import InputField from '@/components/InputField'
import Button from '@/components/Button'

const Login = () => {
  return (
    <View className='flex-1 w-full h-full pt-24 px-6'>
        <Text className='text-primary-100 text-5xl text-center font-bold mb-1'>Welcome back!</Text>
        <Text className='text-xl font-bold text-neutral-400 text-center mb-12'>Enter your credentials.</Text>

        <View className='mb-2 flex gap-4'>
            <InputField
                label='Email'
                placeholder='Email'
                color='primary-400'
            />
            <InputField
                label='Password'
                placeholder='Password'
                color='primary-400'
                password={true}
            />
        </View>

        <Text className='text-md text-primary-400 mb-6 text-right'>Forgot Password?</Text>

        <Button color='primary-100 mb-4 w-full' label='Sign in' />

        <View className='flex flex-row justify-center gap-1'>
            <Text className='text-neutral-400'>New to Verdia?</Text>
            <Text className='text-primary-100 font-bold'>Sign up.</Text>
        </View>
    </View>
  )
}

export default Login

const styles = StyleSheet.create({})