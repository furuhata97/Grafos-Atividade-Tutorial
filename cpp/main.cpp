#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <unordered_map>
#include <list>
#include <queue>
#include <algorithm>
#include <limits>

#include "Vertice.hpp"
using namespace std;

void inicializa(unordered_map<string, Vertice*>& grafo);
void BFS(unordered_map<string, Vertice*>& grafo, string per1);
void articulation_point(unordered_map<string, Vertice*> grafo, string u, list<string>& Q);
void bridges(unordered_map<string, Vertice*> grafo, string u);

int tempo;
string URL = "gameofthrones.txt";

int main() {
	unordered_map<string, Vertice*> grafo;
	
	ifstream arquivo;
	arquivo.open(URL);
	if (!arquivo) {
		cerr << "Erro ao abrir arquivo" << endl;
		return -1;
	}

	string linha;
	string array[3];
	while(getline(arquivo, linha)) {
		stringstream ss(linha);
		for(int i = 0; i < 3; i++) getline(ss, array[i], ',');

		if (grafo.find(array[0]) == grafo.end()) { // se ainda não existe nó pro nome 1, adiciona
			grafo[array[0]] = new Vertice;
			grafo[array[0]]->nome = array[0];
		}
		grafo[array[0]]->addAdj(array[1]);

		if (grafo.find(array[1]) == grafo.end()) { // se ainda não existe o nó pro nome 2, adiciona
			grafo[array[1]] = new Vertice;
			grafo[array[1]]->nome = array[1];
		}
		grafo[array[1]]->addAdj(array[0]);
	}
	arquivo.close();


	int opcao;
	do {
		cout << "******NETWORK OF THRONES******" << endl;
		cout << "Selecione a opção desejada: " << endl;
		cout << "1 - Distância entre dois personagens" << endl;
		cout << "2 - Pontos de articulação" << endl;
		cout << "3 - Pontes" << endl;
		cout << "4 - Sair" << endl;
		cin >> opcao;
		cin.ignore();

		if (opcao == 1) {
			cout << "DISTÂNCIA ENTRE DOIS PERSONAGENS" << endl;
			string per1, per2;
			cout << "Digite o nome dos personagens que deseja calcular a distância: " << endl;
			cout << "Personagem 1: ";
			getline(cin, per1);
			cout << "Personagem 2: ";
			getline(cin, per2);
			BFS(grafo, per1);
			cout << "A distância de " + per1 + " a " + per2 + " é " << grafo[per2]->dist << endl;
			cout << endl;
		}
		if (opcao == 2) {
			tempo = 0;
			inicializa(grafo);
			list<string> Q;
			cout << endl;
			cout << "PONTOS DE ARTICULAÇÃO:" << endl;
			articulation_point(grafo, grafo.begin()->first, Q);
			cout << endl;
		}
		if (opcao == 3) {
			tempo = 0;
			inicializa(grafo);
			cout << endl;
			cout << "PONTES:" << endl;
			bridges(grafo, grafo.begin()->first);
			cout << endl;
		}
	}while (opcao>0 && opcao<4);
}

void inicializa(unordered_map<string, Vertice*>& grafo) {
	for(auto& n: grafo) {
		n.second->cor = BRANCO;
		n.second->dist = numeric_limits<int>::max();
		n.second->pred = nullptr;
		n.second->low = 0;
		n.second->d = 0;
		n.second->f = 0;
	}
}

void BFS(unordered_map<string, Vertice*>& grafo, string per1) {
	inicializa(grafo);

	grafo[per1]->cor = CINZA;
	grafo[per1]->dist = 0;
	grafo[per1]->pred = nullptr;
	
	queue<string> Q;
	Q.push(per1);
	while (!Q.empty()) {
		string s = Q.front();
		Q.pop();
		for (string &v : grafo[s]->adj) {
			if (grafo[v]->cor == BRANCO) {
				grafo[v]->cor = CINZA;
				grafo[v]->dist = grafo[s]->dist + 1;
				grafo[v]->pred = grafo[s];
				Q.push(v);
			}
		}
	}
	grafo[per1]->cor = PRETO;
}

void articulation_point(unordered_map<string, Vertice*> grafo, string u, list<string>& Q) {
	tempo++;
	grafo[u]->cor = CINZA;
	grafo[u]->d = tempo;
	grafo[u]->low = grafo[u]->d;

	for(string &v: grafo[u]->adj) {
		if (grafo[v]->cor == BRANCO){
			grafo[v]->pred = grafo[u];
			articulation_point(grafo, v, Q);

			if (grafo[u]->pred == nullptr) {
				if (v != *(grafo[u]->adj.begin())) { // se v não é a raiz
					if (find(Q.begin(), Q.end(), grafo[u]->nome) == Q.end()) { // se não contém u
						Q.push_back(grafo[u]->nome);
						cout << grafo[u]->nome + " é ponto de articulação" << endl;
					}
				}
			}
			else {
				grafo[u]->low = min(grafo[u]->low, grafo[v]->low);
				if (grafo[v]->low >= grafo[u]->d) {
					if (find(Q.begin(), Q.end(), grafo[u]->nome) == Q.end()) {
						Q.push_back(grafo[u]->nome);
						cout << grafo[u]->nome + " é ponto de articulação" << endl;
					}
				}
			}
		}
		else {
			if ((grafo[v] != grafo[u]->pred) && grafo[v]->d < grafo[u]->d)
				grafo[u]->low = min(grafo[u]->low, grafo[v]->d);
		}
	}
	grafo[u]->cor = PRETO;
	tempo++;
	grafo[u]->f = tempo;
}

void bridges(unordered_map<string, Vertice*> grafo, string u) {
	tempo++;
	grafo[u]->cor = CINZA;
	grafo[u]->d = tempo;
	grafo[u]->low = grafo[u]->d;
	
	for(string &v: grafo[u]->adj) {
		if (grafo[v]->cor == BRANCO){
			grafo[v]->pred = grafo[u];
			bridges(grafo, v);

			grafo[u]->low = min(grafo[u]->low, grafo[v]->low);
			if (grafo[v]->low > grafo[u]->d)
				cout << "Há ponte entre "+grafo[u]->nome + " e " + v << endl;
		}
		else {
			if ((grafo[v] != grafo[u]->pred) && (grafo[v]->d < grafo[u]->d))
				grafo[u]->low = min(grafo[u]->low, grafo[v]->d);
		}
	}
	
	grafo[u]->cor = PRETO;
	tempo++;
	grafo[u]->f = tempo;
}

