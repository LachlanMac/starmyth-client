type = "selfeffect"
interval = 5
life = 0


function _LOOP(data)
  data:getCaster():getStats():changeCurrentHP(3)
  data:getCaster():getStats():changeCurrentEnergy(10)
end

