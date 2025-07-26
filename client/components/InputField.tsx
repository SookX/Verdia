import { KeyboardTypeOptions, Text, TextInput, View } from 'react-native'
import React, { useEffect } from 'react'

interface InputProps {
    label: string,
    placeholder: string,
    color: string,
    password?: boolean
}

const InputField = ({ label, placeholder, color, password } : InputProps) => {

  return (
    <View>
        <Text className='text-sm px-4 text-neutral-300'>{label}</Text>

        <TextInput
            placeholder={placeholder}
            className={`border rounded-full px-4 bg-neutral-700 border-${color}`}
            secureTextEntry={password || false}
        />
    </View>
  )
}

export default InputField