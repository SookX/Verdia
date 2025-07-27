import axios from "axios";
import AsyncStorage from '@react-native-async-storage/async-storage'

axios.defaults.baseURL = process.env.EXPO_PUBLIC_SERVER_ENDPOINT

// Interface for the crud function's arguments
interface CrudInterface {
    url: string,
    method: "get" | "post" | "delete" | "put" | "patch",
    body?: {},
    headers?: {}
}

// Makes a CRUD operation to the backend server
export const crud = async ({ url, method, body, headers }: CrudInterface) => {
    let access = null
    try {
        const token = await AsyncStorage.getItem('access')
        access = token
    } catch(err) {
        console.log(err)
    }

    try {
        const config = {
            headers: access ? {
                'Authorization': `Bearer ${access}`,
                ...headers
            } : {
                ...headers
            }
        }

        let response;
        if (method.toLowerCase() === 'get' || method.toLowerCase() === 'delete') {
            response = await axios[method](url, config);
        } else {
            response = await axios[method](url, body, config);
        }
        
        if(response) return response
    } catch(err) {
        return err
    }
}