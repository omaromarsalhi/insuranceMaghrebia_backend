import pandas as pd
import random

# --- Step 1: Prepare your data ---
# IMPORTANT: Replace this sample data creation with loading your actual CSV file
# For example, if your file is named 'dataset.csv':
# df = pd.read_csv('dataset.csv') # Make sure your CSV file is in the same directory or provide the full path

# Sample DataFrame creation (based on the image you provided)
# PLEASE REPLACE THIS WITH YOUR ACTUAL DATA LOADING:
df = pd.read_csv('healthinsurance.csv')
# num_rows = len(df)
# for x in range(2,num_rows):
#     print(x,df['hereditary_diseases'][x])

# for index, row in df.iterrows():
#     print(row['hereditary_diseases'])

# --- Step 2: Transform the 'claim' column ---
# Ensure 'claim' is numeric before comparison
df['claim'] = pd.to_numeric(df['claim'], errors='coerce')  # Convert to numeric, errors will become NaN

# If 'claim' > 10000, replace with 1, otherwise 0
df['claim'] = df['claim'].apply(lambda x: 1 if pd.notna(x) and x > 10000 else 0)

print("\nDataFrame after 'claim' transformation (head):")
print(df[['city', 'claim']].head())  # Show city alongside for context
print("-" * 30)

# --- Step 3: Transform the 'city' column ---
tunisian_cities = [
    "Tunis", "Sfax", "Sousse", "Kairouan", "Bizerte", "Gabes", "Ariana",
    "Monastir", "Ben Arous", "Kasserine", "Medenine", "Nabeul", "Tataouine",
    "Gafsa", "Jendouba", "Le Kef", "Mahdia", "Sidi Bouzid", "Tozeur",
    "Siliana", "Zaghouan", "Kebili", "Manouba", "Beja", "Hammam Sousse",
    "La Marsa", "Zarzis", "Djerba", "Hammamet",
    "Djerba Island", "Hammam Sousse", "Kairouan", "El Kef"
]

hereditary_diseases = [
    'Diabetes',
    'Heart Disease',
    'Cancer',
    'Genetic Disorders'
]

job=[
    'Office worker',
    'Laborer',
    'Student',
    'Unemployed'
]

num_rows = len(df)
df['city'] = [random.choice(tunisian_cities).lower() for _ in range(num_rows)]
# df['hereditary_diseases'] = [random.choice(hereditary_diseases) for _ in range(3,num_rows) if df['hereditary_diseases'][_] !='NoDisease']
for index, row in df.iterrows():
    if row['hereditary_diseases'] != 'NoDisease':
        df.at[index, 'hereditary_diseases'] = random.choice(hereditary_diseases)

df['job_title'] = [random.choice(job) for _ in range(num_rows)]

# --- Step 4: Export the transformed DataFrame to a CSV file ---
output_filename = 'transformed_dataset.csv'
df.to_csv(output_filename, index=False)
