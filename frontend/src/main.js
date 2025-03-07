// Configuration de l'API
const API_URL = 'http://localhost:8080/student-parser-api/api/students';

// Application Vue
const { createApp, ref, computed, onMounted } = Vue;

const app = createApp({
    data() {
        return {
            // FileUpload
            selectedFile: null,
            isUploading: false,
            isDragOver: false,
            errorMessage: '',

            // StudentList
            students: [],
            currentPage: 1,
            itemsPerPage: 10,
            searchQuery: '',
            sortKey: 'lastName',
            sortOrder: 'asc'
        };
    },

    computed: {
        // Filtrer les étudiants selon la recherche
        filteredStudents() {
            if (!this.students || !this.students.length) return [];

            if (!this.searchQuery) return this.students;

            const query = this.searchQuery.toLowerCase();
            return this.students.filter(student => {
                return (
                    (student.firstName && student.firstName.toLowerCase().includes(query)) ||
                    (student.lastName && student.lastName.toLowerCase().includes(query)) ||
                    (student.studentNumber && student.studentNumber.toLowerCase().includes(query))
                );
            });
        },

        // Trier les étudiants filtrés
        sortedStudents() {
            return [...this.filteredStudents].sort((a, b) => {
                let valueA = a[this.sortKey];
                let valueB = b[this.sortKey];

                // Gestion des valeurs nulles ou undefined
                if (valueA === null || valueA === undefined) valueA = '';
                if (valueB === null || valueB === undefined) valueB = '';

                // Gestion spéciale pour les dates
                if (this.sortKey === 'birthDate') {
                    valueA = valueA ? new Date(valueA).getTime() : 0;
                    valueB = valueB ? new Date(valueB).getTime() : 0;
                } else {
                    // Conversion en chaîne pour les autres types
                    valueA = String(valueA).toLowerCase();
                    valueB = String(valueB).toLowerCase();
                }

                if (this.sortOrder === 'asc') {
                    return valueA > valueB ? 1 : -1;
                } else {
                    return valueA < valueB ? 1 : -1;
                }
            });
        },

        // Paginer les étudiants triés
        paginatedStudents() {
            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;
            return this.sortedStudents.slice(start, end);
        },

        // Liste finale à afficher
        filteredAndSortedStudents() {
            return this.paginatedStudents;
        },

        // Nombre total de pages
        totalPages() {
            return Math.ceil(this.filteredStudents.length / this.itemsPerPage) || 1;
        }
    },

    methods: {
        // --- Méthodes FileUpload ---

        triggerFileInput() {
            this.$refs.fileInput.click();
        },

        handleDragOver(event) {
            this.isDragOver = true;
        },

        validateExcelFile(file) {
            if (!file) return false;

            const validExtensions = ['.xlsx', '.xls'];
            return validExtensions.some(ext => file.name.toLowerCase().endsWith(ext));
        },

        handleFileSelected(event) {
            const file = event.target.files[0];
            this.processSelectedFile(file);
        },

        handleFileDrop(event) {
            const file = event.dataTransfer.files[0];
            this.processSelectedFile(file);
        },

        processSelectedFile(file) {
            if (this.validateExcelFile(file)) {
                this.selectedFile = file;
                this.errorMessage = '';
            } else {
                this.errorMessage = 'Veuillez sélectionner un fichier Excel valide (.xlsx ou .xls)';
                this.selectedFile = null;
            }
        },

        cancelUpload() {
            this.selectedFile = null;
            this.errorMessage = '';
        },

      async uploadFile() {
        if (!this.selectedFile) return;

        this.isUploading = true;
        this.errorMessage = '';

        const formData = new FormData();
        formData.append('file', this.selectedFile);

        try {
          const response = await axios.post(`${API_URL}/parse`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });

          console.log("Réponse de l'API:", response.data); // Ajoutez cette ligne pour déboguer

          if (response.data.success) {
            alert('Fichier importé avec succès');
            // Vérifiez si les données sont dans response.data.data
            if (response.data.data && Array.isArray(response.data.data)) {
              this.students = response.data.data;
            } else {
              console.error("Format de données inattendu:", response.data);
              this.errorMessage = "Format de données inattendu reçu du serveur";
            }
            this.selectedFile = null;
          } else {
            this.errorMessage = response.data.message || 'Erreur lors de l\'importation du fichier';
            alert(this.errorMessage);
          }
        } catch (error) {
                this.errorMessage = error.response?.data?.message || 'Erreur de connexion au serveur';
                alert(this.errorMessage);
                console.error('Upload error:', error);
            } finally {
                this.isUploading = false;
            }
        },

        // --- Méthodes StudentList ---

        formatDate(dateStr) {
            if (!dateStr) return '-';

            const date = new Date(dateStr);
            return isNaN(date.getTime())
                ? dateStr
                : date.toLocaleDateString('fr-FR');
        },

        sortBy(key) {
            // Si on clique sur la même colonne, on inverse l'ordre
            if (this.sortKey === key) {
                this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
            } else {
                this.sortKey = key;
                this.sortOrder = 'asc';
            }

            // Revenir à la première page après le tri
            this.currentPage = 1;
        },

        exportToExcel() {
            if (!this.students || this.students.length === 0) return;

            // Message temporaire
            alert('Fonctionnalité d\'export à implémenter ultérieurement');
        }
    },

    watch: {
        searchQuery() {
            // Revenir à la première page après une recherche
            this.currentPage = 1;
        }
    }
});

// Monter l'application
app.mount('#app');
