interface Person{
    type: string;
    login: string;
    password: string;
    name?: string;
    age?: number;
    cpf?: string;
    companyName?: string;
    cnpj?: string;
    country?: string;
    cep: string;
    state: string;
    description: string;
    skills?: string[];
    approval?: Person[];
    disapproval?: Person[];
}

export { Person }   