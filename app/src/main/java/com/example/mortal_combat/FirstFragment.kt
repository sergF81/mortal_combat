package com.example.mortal_combat


import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mortal_combat.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {
    private var mAnimationDrawableMonster: AnimationDrawable? = null
    private var mAnimationDrawableUser: AnimationDrawable? = null
    private var resultTurn = 0
    private var orderOfKick = 1;
    private val DURATION = 150
    private lateinit var fightSound: MediaPlayer
    private lateinit var kickSound: MediaPlayer
    private val userHero = UserHero(
        "Lu Kang", 10, 14, 10, 2..4,
        arrayOf(
            "lready1",
            "lpain",
            "lblock",
            "lkick1",
            "lkick2",
            "lkick3",
            "llose1",
            "llose2",
            "llose3"
        )
    )
    private val monster = Monster(
        "Scorpion", 10, 16, 10, 4..6,
        arrayOf(
            "sready1",
            "spain",
            "sblock",
            "skick1",
            "skick2",
            "skick3",
            "slose1",
            "slose2",
            "slose3"
        )
    )

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kickSound = MediaPlayer.create(context, R.raw.kick)
        fightSound = MediaPlayer.create(context, R.raw.fight)

        binding.textViewMonsterHealth.text = "${monster.healthCurrent}/${monster.healthMax}"
        binding.textViewUserHealth.text = "${userHero.healthCurrent}/${userHero.healthMax}"
        binding.textViewRestoreTime.text = userHero.restoreTime.toString()
        binding.textViewUserName.text = userHero.name
        binding.textViewMonsterName.text = monster.name
        binding.buttonRestart.setOnClickListener {
            val action =
                FirstFragmentDirections.actionFirstFragmentSelf()
            findNavController().navigate(action)
        }
        binding.buttonTurnKick.setOnClickListener {
            kick()
            startAnimateFight(resultTurn)
            changeHealth()
            startAnimateFight(resultTurn)
        }
        binding.buttonAddHealth.setOnClickListener {
            println("До: ${userHero.healthCurrent}")
            userHero.healthCurrent =
                userHero.restoreLife(userHero.healthCurrent, userHero.healthMax)
            println("После: ${userHero.healthCurrent}")
            binding.textViewUserHealth.text = "${userHero.healthCurrent}/${userHero.healthMax}"
            binding.textViewRestoreTime.text = userHero.restoreTime.toString()
            if (userHero.restoreTime == 0) binding.buttonAddHealth.isEnabled = false
        }
        fightSound.start()
    }

    private fun changeHealth() {
        if (orderOfKick == 2) {
            if (monster.healthCurrent <= 0) {
                binding.textViewMonsterHealth.text = "LOSE"
                binding.buttonTurnKick.visibility = View.INVISIBLE
                resultTurn = 5
                binding.buttonRestart.visibility = View.VISIBLE
                binding.textViewMessageEnd.visibility = View.VISIBLE
                binding.textViewMessageEnd.text = "You Win!"
            } else
                binding.textViewMonsterHealth.text = "${monster.healthCurrent}/${monster.healthMax}"
        } else {
            if (userHero.healthCurrent <= 0) {
                binding.textViewUserHealth.text = "LOSE"
                binding.buttonTurnKick.visibility = View.INVISIBLE
                resultTurn = 6
                binding.textViewMessageEnd.visibility = View.VISIBLE
                binding.textViewMessageEnd.text = "You Lose!"
                binding.buttonRestart.visibility = View.VISIBLE
            } else
                binding.textViewUserHealth.text = "${userHero.healthCurrent}/${userHero.healthMax}"
        }
    }

    private fun kick() {
        var modificatorOfAttack = 0
        if (orderOfKick == 1) {
            //Рассчитываем Модификатор атаки. Он равен разности Атаки атакующего и Защиты защищающегося плюс 1
            modificatorOfAttack = userHero.attack - monster.defense + 1
            if (modificatorOfAttack <= 0) modificatorOfAttack = 1
            println("____________________________________________")
            println("Атакует: " + userHero.name + " c модификатором атаки " + modificatorOfAttack)
            //рассчитываем успешен ли был удар или нет
            for (i in 1..modificatorOfAttack) {
                val rangeOfCube = 1..6
                val numberOfCube = rangeOfCube.random()
                println("Random number: $numberOfCube")
                if (numberOfCube >= 5) {
                    val resultOfKick = userHero.damage.random()
                    monster.healthCurrent = monster.healthCurrent - resultOfKick
                    resultTurn = 1
                    orderOfKick = 2
                    println("Удар прошел: $numberOfCube")
                    break
                } else {
                    orderOfKick = 2
                    resultTurn = 2
                    println("Блокировка удара: $numberOfCube")

                }
            }
        } else if (orderOfKick == 2) {
            //Рассчитываем Модификатор атаки. Он равен разности Атаки атакующего и Защиты защищающегося плюс 1
            modificatorOfAttack = monster.attack - userHero.defense + 1
            if (modificatorOfAttack <= 0) modificatorOfAttack = 1
            println("Атакует: " + monster.name + " c модификатором атаки " + modificatorOfAttack)
            //рассчитываем успешен ли был удар или нет
            for (i in 1..modificatorOfAttack) {
                val rangeOfCube = 1..6
                val numberOfCube = rangeOfCube.random()
                println("Random number: $numberOfCube")
                if (numberOfCube >= 5) {
                    val resultOfKick = monster.damage.random()
                    userHero.healthCurrent = userHero.healthCurrent - resultOfKick
                    resultTurn = 3
                    orderOfKick = 1
                    println("Удар прошел: $numberOfCube. Результат хода: $resultTurn")

                    break
                } else {
                    resultTurn = 4
                    orderOfKick = 1
                    println("Блокировка удара: $numberOfCube. Результат хода: $resultTurn")
                }
            }
        }
        kickSound.start()
    }


    private fun startAnimateFight(resultTurn: Int) {

        var resIdforKickUser1: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[3],
                null,
                "com.example.mortal_combat"
            )
        val resIdforKickUser2: Int = resources.getIdentifier(
            "@drawable/" + userHero.frameArray[4],
            null,
            "com.example.mortal_combat"
        )
        val resIdforKickUser3: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[5],
                null,
                "com.example.mortal_combat"
            )
        val resIdforReadyUser: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[0],
                null,
                "com.example.mortal_combat"
            );
        val resIdforPainUser: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[1],
                null,
                "com.example.mortal_combat"
            );
        val resIdforBlockUser: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[2],
                null,
                "com.example.mortal_combat"
            );
        val resIdforLlose1User: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[6],
                null,
                "com.example.mortal_combat"
            );
        val resIdforLlose2User: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[7],
                null,
                "com.example.mortal_combat"
            );
        val resIdforLlose3User: Int =
            resources.getIdentifier(
                "@drawable/" + userHero.frameArray[8],
                null,
                "com.example.mortal_combat"
            );


        val resIdforKickMonster1: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[3],
                null,
                "com.example.mortal_combat"
            );
        var resIdforKickMonster2: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[4],
                null,
                "com.example.mortal_combat"
            );
        var resIdforKickMonster3: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[5],
                null,
                "com.example.mortal_combat"
            );
        var resIdforReadyMonster: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[0],
                null,
                "com.example.mortal_combat"
            );
        var resIdforPainMonster: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[1],
                null,
                "com.example.mortal_combat"
            )
        var resIdforBlockMonster: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[2],
                null,
                "com.example.mortal_combat"
            )
        var resIdforSlose1Monster: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[6],
                null,
                "com.example.mortal_combat"
            )
        var resIdforSlose2Monster: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[7],
                null,
                "com.example.mortal_combat"
            )
        var resIdforSlose3Monster: Int =
            resources.getIdentifier(
                "@drawable/" + monster.frameArray[8],
                null,
                "com.example.mortal_combat"
            )


        when (resultTurn) {

            1, 2 -> {
                var frame1 = resources.getDrawable(resIdforKickUser1, null) as BitmapDrawable
                var frame2 = resources.getDrawable(resIdforKickUser2, null) as BitmapDrawable
                var frame3 = resources.getDrawable(resIdforKickUser3, null) as BitmapDrawable
                var frame4 = resources.getDrawable(resIdforReadyUser, null) as BitmapDrawable
                var frame5 = if (resultTurn == 1) {
                    resources.getDrawable(resIdforPainMonster, null) as BitmapDrawable
                } else {
                    resources.getDrawable(resIdforBlockMonster, null) as BitmapDrawable
                }
                var frame6 = resources.getDrawable(resIdforReadyMonster, null) as BitmapDrawable
                mAnimationDrawableUser = AnimationDrawable()
                mAnimationDrawableUser?.isOneShot = true
                mAnimationDrawableUser?.addFrame(frame1, DURATION)
                mAnimationDrawableUser?.addFrame(frame2, DURATION)
                mAnimationDrawableUser?.addFrame(frame3, DURATION)
                mAnimationDrawableUser?.addFrame(frame2, DURATION)
                mAnimationDrawableUser?.addFrame(frame1, DURATION)
                mAnimationDrawableUser?.addFrame(frame4, DURATION)
                binding.userView.setImageDrawable(mAnimationDrawableUser)

                if (!mAnimationDrawableUser?.isRunning!!) {
                    mAnimationDrawableUser?.setVisible(true, true)
                    mAnimationDrawableUser?.start()
                }

                mAnimationDrawableMonster = AnimationDrawable()
                mAnimationDrawableMonster?.isOneShot = true
                mAnimationDrawableMonster?.addFrame(frame6, 300)
                mAnimationDrawableMonster?.addFrame(frame5, 500)
                mAnimationDrawableMonster?.addFrame(frame6, DURATION)
                binding.monsterView.setImageDrawable(mAnimationDrawableMonster)
                if (!mAnimationDrawableMonster?.isRunning!!) {
                    mAnimationDrawableMonster?.setVisible(true, true)
                    mAnimationDrawableMonster?.start()
                }
            }
            3, 4 -> {
                var frame1 = resources.getDrawable(resIdforKickMonster1, null) as BitmapDrawable
                var frame2 = resources.getDrawable(resIdforKickMonster2, null) as BitmapDrawable
                var frame3 = resources.getDrawable(resIdforKickMonster3, null) as BitmapDrawable
                var frame4 = resources.getDrawable(resIdforReadyMonster, null) as BitmapDrawable
                var frame5 = resources.getDrawable(resIdforPainUser, null) as BitmapDrawable
                if (resultTurn == 4) {
                    frame5 = resources.getDrawable(resIdforBlockUser, null) as BitmapDrawable
                }
                var frame6 = resources.getDrawable(resIdforReadyUser, null) as BitmapDrawable

                mAnimationDrawableMonster = AnimationDrawable()
                mAnimationDrawableMonster?.isOneShot = true
                mAnimationDrawableMonster?.addFrame(frame1, DURATION)
                mAnimationDrawableMonster?.addFrame(frame2, DURATION)
                mAnimationDrawableMonster?.addFrame(frame3, DURATION)
                mAnimationDrawableMonster?.addFrame(frame2, DURATION)
                mAnimationDrawableMonster?.addFrame(frame1, DURATION)
                mAnimationDrawableMonster?.addFrame(frame4, DURATION)
                binding.monsterView.setImageDrawable(mAnimationDrawableMonster)

                if (!mAnimationDrawableMonster?.isRunning!!) {
                    mAnimationDrawableMonster?.setVisible(true, true)
                    mAnimationDrawableMonster?.start()
                }

                mAnimationDrawableUser = AnimationDrawable()
                mAnimationDrawableUser?.isOneShot = true
                mAnimationDrawableUser?.addFrame(frame6, 300)
                mAnimationDrawableUser?.addFrame(frame5, 500)
                mAnimationDrawableUser?.addFrame(frame6, DURATION)
                binding.userView.setImageDrawable(mAnimationDrawableUser)
                if (!mAnimationDrawableUser?.isRunning!!) {
                    mAnimationDrawableUser?.setVisible(true, true)
                    mAnimationDrawableUser?.start()
                }
            }
            5 -> {
                var frame1 = resources.getDrawable(resIdforSlose1Monster, null) as BitmapDrawable
                var frame2 = resources.getDrawable(resIdforSlose2Monster, null) as BitmapDrawable
                var frame3 = resources.getDrawable(resIdforSlose3Monster, null) as BitmapDrawable
                mAnimationDrawableMonster = AnimationDrawable()
                mAnimationDrawableMonster?.isOneShot = true
                mAnimationDrawableMonster?.addFrame(frame1, DURATION)
                mAnimationDrawableMonster?.addFrame(frame2, DURATION)
                mAnimationDrawableMonster?.addFrame(frame3, DURATION)
                binding.monsterView.setImageDrawable(mAnimationDrawableMonster)
                if (!mAnimationDrawableMonster?.isRunning!!) {
                    mAnimationDrawableMonster?.setVisible(true, true)
                    mAnimationDrawableMonster?.start()
                }
            }
            6 -> {
                var frame1 = resources.getDrawable(resIdforLlose1User, null) as BitmapDrawable
                var frame2 = resources.getDrawable(resIdforLlose2User, null) as BitmapDrawable
                var frame3 = resources.getDrawable(resIdforLlose3User, null) as BitmapDrawable
                mAnimationDrawableUser = AnimationDrawable()
                mAnimationDrawableUser?.isOneShot = true
                mAnimationDrawableUser?.addFrame(frame1, DURATION)
                mAnimationDrawableUser?.addFrame(frame2, DURATION)
                mAnimationDrawableUser?.addFrame(frame3, DURATION)
                binding.userView.setImageDrawable(mAnimationDrawableUser)
                if (!mAnimationDrawableUser?.isRunning!!) {
                    mAnimationDrawableUser?.setVisible(true, true)
                    mAnimationDrawableUser?.start()
                }
            }
        }
    }
}

