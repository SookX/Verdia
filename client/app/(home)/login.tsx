import { Image, StyleSheet, Text, View, ImageBackground, TextInput, TouchableOpacity } from 'react-native'
import React from 'react'
import { images } from '@/constants/images'

const Login = () => {
  return (
    <View className='flex-1 w-full h-full pt-24 px-6'>
        <Text className='text-primary-100 text-5xl text-center font-bold mb-1'>Welcome back!</Text>
        <Text className='text-xl font-bold text-neutral-400 text-center mb-[20%]'>Enter your credentials.</Text>

        <View className='mb-12'>
            <View>
                <Text className='text-sm px-4 text-neutral-300'>Email</Text>

                <TextInput
                    placeholder='Email'
                    className='border rounded-full px-4 bg-neutral-700 border-primary-400'
                />
            </View>
        </View>

        <TouchableOpacity className='w-full py-3 px-6 rounded-full bg-primary-100'>
            <Text className='text-xl text-light-100 text-center uppercase font-bold'>Sign in</Text>
        </TouchableOpacity>
    </View>
  )
}

export default Login

const styles = StyleSheet.create({})