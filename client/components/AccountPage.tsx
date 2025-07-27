import { Image, StyleSheet, Text, View, ImageBackground, TextInput, TouchableOpacity, FlatList, ScrollView } from 'react-native'
import React from 'react'
import { images } from '@/constants/images'
import InputField from '@/components/InputField'
import Button from '@/components/Button'
import { Link, RelativePathString } from 'expo-router'

interface AccountInterface {
    title: string,
    text: string,
    inputs: {
            label: string,
            placeholder?: string,
            color: string,
            value?: string,
            setValue?: () => void
        } [],
    forgotPass: boolean,
    buttonLabel: string,
    handleSubmit?: () => void,
    link: {
        text: string,
        label: string,
        link: RelativePathString
    }
}

const AccountPage = ({ title, text, inputs, forgotPass, buttonLabel, handleSubmit, link }: AccountInterface) => {
  return (
    <ImageBackground className='flex-1 w-full justify-center items-center' source={images.home} resizeMode='cover'>
        <View className='flex-1 bg-black opacity-50 absolute z-0 w-full h-full'/>
        <Image
            source={images.logo}
            className='w-[75%] h-48 absolute top-32 translate-y-[-50%]'
            resizeMode='contain'
        />

        <ScrollView className='flex-1 w-full h-full py-16 px-6 bg-white mt-64 rounded-t-[30px]'>
            <View>
                <Text className='text-primary-100 text-5xl text-center font-bold mb-1'>{title}</Text>
                <Text className='text-xl text-neutral-600 text-center mb-12'>{text}</Text>
            </View>

            <View>
                <FlatList
                    data={inputs}
                    renderItem={({item}) => (
                        <InputField
                            label={item.label}
                            placeholder={item.placeholder}
                            color={item.color}
                            value={item?.value}
                            setValue={item?.setValue}
                        />
                    )}
                    keyExtractor={(item, i) => i.toString()}
                />
                {forgotPass && <Text className='text-md text-primary-400 mb-6 text-right'>Forgot Password?</Text>}
            </View>

            <View className='mt-auto'>

                <Button color='primary-100' styling='mb-4 w-full' label={buttonLabel} onPress={handleSubmit} />

                <View className='flex flex-row justify-center gap-1'>
                    <Text className='text-neutral-400'>{link.text}</Text>
                    <Link href={link.link} className='text-primary-100 font-bold'>{link.label}</Link>
                </View>
            </View>
        </ScrollView>
    </ImageBackground>
  )
}

export default AccountPage

const styles = StyleSheet.create({})