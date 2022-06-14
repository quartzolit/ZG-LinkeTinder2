interface Person{
    type: string;
    email: string;
    password: string;
    name?: string;
    //age?: number;
    dob?: Date;
    cpf?: string;
    companyName?: string;
    cnpj?: string;
    country: string;
    cep: string;
    state: string;
    description: string;
    skills?: string[];
    vacancy?: string;
    approval?: Person[];
    disapproval?: Person[];
}

export { Person }   