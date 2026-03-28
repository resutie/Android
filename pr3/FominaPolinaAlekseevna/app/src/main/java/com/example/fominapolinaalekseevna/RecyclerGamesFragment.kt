package com.example.fominapolinaalekseevna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerGamesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recycler_games, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.gamesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // В каждом элементе есть картинка + текст, как требует методичка.
        val items = listOf(
            GameItem(
                "Каркассон",
                "Тактическая укладка тайлов и контроль территорий.",
                "https://www.igroved.ru/db/games/images/44/944/igroved_carcassonne-korolevskiy-podarok_06.jpg"
            ),
            GameItem(
                "Шахматы",
                "Классическая стратегическая дуэль.",
                "https://upload.wikimedia.org/wikipedia/commons/d/d9/Opening_chess_position_from_black_side.jpg"
            ),
            GameItem(
                "Catan",
                "Экономическая настолка про развитие поселений.",
                "https://images.firma-gamma.ru/images/4/4/d34722018632u_2.jpg"
            ),
            GameItem(
                "Dixit",
                "Ассоциативная игра с красивыми картами.",
                "https://www.igroved.ru/db/games/images/71/3371/igroved_dixit-odyssey_03.jpg"
            )
        )

        recyclerView.adapter = GameRecyclerAdapter(items)
    }
}
