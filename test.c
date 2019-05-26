extern int a = 0;

int main(){
    int hhh = 3;
    func(3);
    return 0;
}

int funcb(int b, int c){
    int *ptr = a;
    double c = 3.2;
    int y = 2*(3+ 4);
    int d = b+c*a;
    int i;
    string str = "abcd";
    switch(a){
        case 0: c++; break;
        case 1: c--;break;
        default: c= 0; break;
    }
    for(i = 0; i < 10; i++){
        int j = i;
        a++;
    }
    while(a > 0){
        int h = a;
        a--;
    }
    if(b != c){
        return a;
    }
    else{
        return b;
    }
}