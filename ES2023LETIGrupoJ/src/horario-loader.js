// Importa a biblioteca PapaParse para fazer o parse do CSV
import Papa from 'papaparse';

// Elemento HTML onde a tabela será renderizada
const tableContainer = document.getElementById('horario-table');

// Caminho para o arquivo CSV
const csvFilePath = 'horario-exemplo.csv';

// Função para carregar o CSV e criar a tabela Tabulator
Papa.parse(csvFilePath, {
    header: true,
    download: true,
    dynamicTyping: true,
    complete: function (results) {
        const data = results.data;

        // Cria a tabela Tabulator
        const table = new Tabulator(tableContainer, {
            data: data,
            layout: 'fitColumns',
            columns: [
                { title: 'Curso', field: 'Curso' },
                { title: 'Unidade Curricular', field: 'Unidade Curricular' },
                { title: 'Turno', field: 'Turno' },
                { title: 'Turma', field: 'Turma' },
                { title: 'Inscritos no turno', field: 'Inscritos no turno' },
                { title: 'Dia da semana', field: 'Dia da semana' },
                { title: 'Hora início da aula', field: 'Hora início da aula' },
                { title: 'Hora fim da aula', field: 'Hora fim da aula' },
                { title: 'Data da aula', field: 'Data da aula' },
                { title: 'Características da sala pedida', field: 'Características da sala pedida para a aula' },
                { title: 'Sala atribuída', field: 'Sala atribuída à aula' },
                // Adicione colunas para as outras informações conforme necessário
            ],
        });
    },
});
