import { KeyboardTypeOptions, Text, TextInput, View } from 'react-native'
import React, { useEffect } from 'react'

interface InputProps {
    label: string,
    placeholder?: string,
    color: string,
    password?: boolean
}

const InputField = ({ label, placeholder, color, password } : InputProps) => {
    const colorMap: Record<string, string> = {
        "primary-400": "border-primary-400"
    }

    const borderColor = colorMap[color] || "border-primary-400"
    const textColor = colorMap[color] || "color-primary-400"

    return (
        <View className='mb-4'>
            <Text className='text-sm px-4 mb-1 text-neutral-300'>{label}</Text>

            <TextInput
                // placeholder={placeholder}
                className={`border ${borderColor} rounded-full px-4 py-3 ${textColor}`}
                secureTextEntry={password || false}
            />
        </View>
    )
}

export default InputField