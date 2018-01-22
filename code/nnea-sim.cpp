#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

enum SUITS
{
    CLUBS,
    DIAMONDS,
    SPADES,
    HEARTS
};

int suit(int c)
{
    return c/13;
}
int value(int c)
{
    return c%13;
}
int _rank(int c)
{
    return (c+12)%13;
}
char sc[4] = {5,4,6,3};
char vc[13] = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K'};
void print_c(int c)
{
    printf("%c%c",vc[value(c)],sc[suit(c)]);
}
void print_ca(bool arr[])
{
    int ps = -1;
    for (int i = 0; i < 52; ++i)
    {
        if (arr[i])
        {
            if (ps != suit(i))
            {
                ps = suit(i);
                printf(" %c", sc[suit(i)]);
            }
            printf("%c", vc[value(i)]);
        }
    }
}
int point_value(int c)
{
    if (suit(c) == HEARTS) return 1;
    if (suit(c) == SPADES && value(c) == 11) return 13;
    return 0;
}

struct higher_value
{
    bool operator()(const int &lhs, const int &rhs) const
    {
        return value(lhs) < value(rhs);
    }
};

struct player
{
    bool hand[52];
    bool played[52];
    bool taken[52];
    double utility[52];
    double weights[37856];
    int selection;
    int total_points;
};
void init_player(player &p)
{
    for (int i = 0; i < 52; ++i)
    {
        p.hand[i] = false;
        p.played[i] = false;
        p.taken[i] = false;
        p.utility[i] = 0.0;
    }
    p.selection = -1;
}
void rand_weights(player& p)
{
    for (int i = 0; i < 37856; ++i)
    {
        p.weights[i] = (double)rand() / (double)RAND_MAX * 10.0 - 5.0;
    }
}
void breed_weights(player& p, const player &father, const player &mother)
{
    for (int i = 0; i < 37856; ++i)
    {
        p.weights[i] = (rand()%2)?
            father.weights[i] : mother.weights[i];
    }
}
void mutate_weights(player &p)
{
    for (int i = 0; i < 37856; ++i)
        if (rand() == 0)
            p.weights[i] = (double)rand() / (double)RAND_MAX * 10.0 - 5.0;
}

int lower_points(const void* lhs, const void* rhs)
{
    if(((player*)lhs)->total_points < ((player*)rhs)->total_points)
        return -1;
    if(((player*)lhs)->total_points == ((player*)rhs)->total_points)
        return 0;
    return 1;
}


player players[4];

void deal()
{
    int arr[52];
    for (int i = 0; i < 52; ++i)
        arr[i] = i;
    for (int i = 0; i < 52; ++i)
    {
        int o = rand() % 52;
        int t = arr[o];
        arr[o] = arr[i];
        arr[i] = t;
    }
    for (int i = 0; i < 52; ++i)
    {
        players[i%4].hand[arr[i]] = true;
    }
}

double sigmoid(double d)
{
    double expd = exp(d);
    return (expd-1.0)/(expd+1.0);
}

bool hearts_broken;
int lead;
int leader;
int last_leader;

bool valid(int c)
{
    if (lead >= 0)
        return suit(c) == suit(lead);
    else
        return suit(c) != HEARTS || hearts_broken;
}

void select(player &p)
{
    double first[312];
    double hidden[104];

    for (int i = 0; i < 52; ++i)
    {
        first[i] = p.hand[i] ? 1.0 : 0.0;
        first[i+52] = p.taken[i] ? 1.0 : 0.0;
    }
    for (int j = 0; j < 4; ++j)
        for (int i = 0; i < 52; ++i)
            first[i + j*52 + 104] = players[j].played[i] ? 1.0 : 0.0;

    for (int i = 0; i < 104; ++i)
    {
        hidden[i] = 0;
        for (int j = 0; j < 312; ++j)
        {
            hidden[i] += p.weights[i * 312 + j] * first[j];
        }
        hidden[i] = sigmoid(hidden[i]);
    }
    for (int i = 0; i < 52; ++i)
    {
        p.utility[i] = 0.0;
        for (int j = 0; j < 104; ++j)
        {
            p.utility[i] += p.weights[i * 104 + j + 32448] * hidden[j];
        }
    }

    int best = -1;
    for (int i = 0; i < 52; ++i)
    {
        if (valid(i) && p.hand[i])
        {
            if (best == -1)
                best = i;
            else if (p.utility[i] > p.utility[best])
                best = i;
        }
    }
    if (best == -1)
    {
        for (int i = 0; i < 52; ++i)
        {
            if (p.hand[i])
            {
                if (best == -1)
                    best = i;
                else if (p.utility[i] > p.utility[best])
                    best = i;
            }
        }
    }
    if (suit(best) == HEARTS)
        hearts_broken = true;
    p.selection = best;
}

void play_trick()
{
    int highest = leader;
    int best;

    select(players[leader]);
    lead = players[leader].selection;
    best = lead;
    players[leader].hand[lead] = false;
    players[leader].played[lead] = true;
    for (int i = 1; i < 4; ++i)
    {
        select(players[(leader+i)%4]);
        int c = players[(leader+i)%4].selection;
        players[(leader+i)%4].hand[c] = false;
        players[(leader+i)%4].played[c] = true;

        if (suit(c) == suit(best) && _rank(c) > _rank(best))
        {
            highest = (leader+i)%4;
            best = c;
        }
    }

    last_leader = leader;
    leader = highest;
    for (int i = 0; i < 4; ++i)
    {
        players[leader].taken[players[i].selection] = true;
    }
}

#define NUM_PLAYERS 1000
#define QUARTER_PLAYERS (NUM_PLAYERS/4)

player all_players[NUM_PLAYERS];
int primes[] = {1,2,3,5,7,11,13,17,19,23,29,31,37,41,43,47};

FILE * file;

int main()
{
    srand(time(NULL));

    printf("%d", sizeof(player));

    for (int i = 0; i < NUM_PLAYERS; ++i)
        rand_weights(all_players[i]);

    for (int w = 0; w < 1000; ++w)
    {
        for (int i = 0; i < NUM_PLAYERS; ++i)
            all_players[i].total_points = 0;

        for (int y = 0; y < 16; ++y)
        {
            for (int z = 0; z < QUARTER_PLAYERS; ++z)
            {
                if (rand()%10 == 0)
                    printf("\rplaying game %d               ", y*QUARTER_PLAYERS + z);
                int froms[4];
                for (int i = 0; i < 4; ++i)
                {
                    froms[i] = (z * 4 + i * primes[y])%NUM_PLAYERS;
                    memcpy(players + i, all_players + froms[i], sizeof(player));
                    init_player(players[i]);
                    players[i].total_points = 0;
                }

                deal();

                for (int i = 0; i < 4; ++i)
                {
                    //printf("\nPlayer %d dealt:", i);
                    //print_ca(players[i].hand);
                }

                hearts_broken = false;
                lead = -1;
                leader = 0;

                for (int j = 0; j < 13; ++j)
                {
                    //printf("\n  %d", leader);

                    play_trick();

                    for (int i = 0; i < 4; ++i)
                    {
                        //printf("\t %d : ", (i+last_leader)%4);
                        //print_c(players[(i+last_leader)%4].selection);
                    }
                }
                //printf("\n");
                int p[4] = {0,0,0,0};
                for (int i = 0; i < 4; ++i)
                {
                    for (int j = 0; j < 52; ++j)
                        if (players[i].taken[j])
                            p[i] += point_value(j);
                    //printf("\t%d: +%d", i, p[i]);
                }
                for (int i = 0; i < 4; ++i)
                {
                    if (p[i] == 26)
                    {
                        players[0].total_points += p[i];
                        players[1].total_points += p[i];
                        players[2].total_points += p[i];
                        players[3].total_points += p[i];
                        players[i].total_points -= p[i];
                    }
                    else
                        players[i].total_points += p[i];
                }
                for (int i = 0; i < 4; ++i)
                {
                    all_players[froms[i]].total_points += players[i].total_points;
                }
            }
        }

        printf("\rsorting players         ");
        qsort(all_players, NUM_PLAYERS, sizeof(player),lower_points);
        printf("\r                      -=-=-=- GENERATION %4d -=-=-=-\n", w);
        for (int i = 0; i < 7; ++i)
            printf("%4d: %4d", i, all_players[i].total_points);
        printf("\n");
        for (int i = 7; i < 14; ++i)
            printf("%4d: %4d", i, all_players[i].total_points);
        printf("\n");
        for (int i = 0; i < 7; ++i)
            printf("%4d: %4d", i+QUARTER_PLAYERS, all_players[i+QUARTER_PLAYERS].total_points);
        printf("\n");
        for (int i = 0; i < 7; ++i)
            printf("%4d: %4d", i+QUARTER_PLAYERS+QUARTER_PLAYERS, all_players[i+QUARTER_PLAYERS+QUARTER_PLAYERS].total_points);
        printf("\n");
        printf("\n");
        for (int i = QUARTER_PLAYERS; i < NUM_PLAYERS; ++i)
        {
            breed_weights(all_players[i],all_players[rand()%QUARTER_PLAYERS],all_players[rand()%QUARTER_PLAYERS]);
            mutate_weights(all_players[i]);

            if (rand()%10 == 0)
                printf("\rrewriting player %d", i);
        }

        file = fopen("best.txt", "w");
        if (file != NULL)
        {
            for (int i = 0; i < 37856; ++i)
                fprintf(file, "%f\n", all_players[0].weights[i]);
            fclose(file);
            file = NULL;
        }
        else
            printf("Failed to open file to write!\n");
    }
}
