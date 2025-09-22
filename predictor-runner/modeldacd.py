import sys
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import r2_score, mean_squared_error, mean_absolute_error
from sklearn.linear_model import LinearRegression
from sklearn.neighbors import KNeighborsRegressor
from sklearn.svm import SVR
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor

if len(sys.argv) < 2:
    sys.exit(1)
ruta_datamart = sys.argv[1]

df = pd.read_csv(ruta_datamart).drop_duplicates()

cryptos = df['coin_name'].unique()
targets = [
    'price', 'volumeChange24h', 'percentChange1h', 'percentChange24h',
    'percentChange7d', 'percentChange30d', 'percentChange60d', 'percentChange90d'
]

best_params_general = {
    'LinearRegression': {},
    'KNN': {'n_neighbors': 5},
    'SVM': {'C': 1, 'kernel': 'rbf'},
    'DecisionTree': {'max_depth': 5},
    'RandomForest': {'n_estimators': 100, 'max_depth': 10}
}

models = {
    'LinearRegression': LinearRegression(**best_params_general['LinearRegression']),
    'KNN': KNeighborsRegressor(**best_params_general['KNN']),
    'SVM': SVR(**best_params_general['SVM']),
    'DecisionTree': DecisionTreeRegressor(**best_params_general['DecisionTree']),
    'RandomForest': RandomForestRegressor(**best_params_general['RandomForest'])
}

print("Coin,Target,Model,R2,MSE,MAE,Mean,Std,Var")

for crypto in cryptos:
    for target in targets:
        data = df[df['coin_name'] == crypto]
        X = data[['stars', 'forks', 'issuesAndPullRequest', 'watchers']]
        y = data[target]

        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=27)

        scaler = MinMaxScaler()
        X_train_scaled = scaler.fit_transform(X_train)
        X_test_scaled = scaler.transform(X_test)

        for name, model in models.items():
            model.fit(X_train_scaled, y_train)
            y_pred = model.predict(X_test_scaled)

            r2 = r2_score(y_test, y_pred)
            mse = mean_squared_error(y_test, y_pred)
            mae = mean_absolute_error(y_test, y_pred)
            mean_pred = np.mean(y_pred)
            std_pred = np.std(y_pred)
            var_pred = np.var(y_pred)

            print(f"{crypto},{target},{name},{r2:.4f},{mse:.4f},{mae:.4f},{mean_pred},{std_pred:.4f},{var_pred:.4f}")