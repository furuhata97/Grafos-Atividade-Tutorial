#include <string>
#include <list>
using namespace std;

enum Cor {
	BRANCO,
	CINZA,
	PRETO
};

class Vertice;
class Vertice {
	public:
	
    int dist;
    list<string> adj;
    Vertice* pred;
    Cor cor;
    int d;
    int f;
    int low;
    string nome;

	void addAdj(const string& v) {
		adj.push_back(v);
	}
};