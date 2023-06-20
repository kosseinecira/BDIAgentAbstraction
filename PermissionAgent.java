package com.permissionagent.fullapp.agents.pemission_agent;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.permissionagent.fullapp.activities.login_environment.LoginActivity;
import com.permissionagent.fullapp.activities.permission_environment.PermissionActivity;
import com.permissionagent.fullapp.agentabstraction.Agent;
import com.permissionagent.fullapp.agentabstraction.Belief;
import com.permissionagent.fullapp.agentabstraction.Desire;
import com.permissionagent.fullapp.agentabstraction.DesireState;
import com.permissionagent.fullapp.agentabstraction.Plan;
import com.permissionagent.fullapp.agents.pemission_agent.beliefs.LocationPermission;
import com.permissionagent.fullapp.agents.pemission_agent.beliefs.StoragePermission;
import com.permissionagent.fullapp.agents.pemission_agent.plans.HandleLocationPermissionPlan;
import com.permissionagent.fullapp.agents.pemission_agent.plans.HandleStoragePermissionPlan;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PermissionAgent extends Agent {
    //Each agent extends Agent abstract class which has Environment super class ,
    //Each agent Defining local Environment , local Environment extends Agent's abstract class's Environment.
    //Each agent has to Downcast the super class's environment so it can access to it's local interfaces ,
    //so that agent can perform actions to it's Environment.
    //Also , this doesn't mean that we have changed the environment or something, it just a design necessity due to OOP paradigm.
    // we must define all desires or goals that the agent should achieve at the first time. as attributes
    private PermissionActivity localEnvironment;

    private Desire grantLocationPermissionDesire = new Desire("grantLocationPermissionDesire", 0);
    HandleLocationPermissionPlan handleLocationPermissionPlan = new HandleLocationPermissionPlan("handleLocationPermissionPlan",
            grantLocationPermissionDesire,
            this, getEnvironment());

    //Defining plans to achieve goals;
    private Desire grantStoragePermissionDesire = new Desire("grantStoragePermissionDesire", 1);
    HandleStoragePermissionPlan handleStoragePermissionPlan = new HandleStoragePermissionPlan("handleStoragePermissionPlan",
            grantStoragePermissionDesire,
            this, getEnvironment());


    /**
     * DownCasting steps:
     * Parent parent = new Child();
     * Child child = (Child) parent:
     * <p>
     * AppCompatActivity appCompat = localEnvironment // that has been passed through the constructor
     * LocalEnvironment localEnvironment = (LocalEnvironment)appCompat;
     */

    public PermissionAgent(String agentName, AppCompatActivity environment) {
        super(agentName, environment);
        //Adding desires to desire set!
        getDesireSet().put(grantLocationPermissionDesire.getName(), grantLocationPermissionDesire);
        getDesireSet().put(grantStoragePermissionDesire.getName(), grantStoragePermissionDesire);
        //Adding plans to plans repository
        //casting environment
        this.localEnvironment = (PermissionActivity) getEnvironment();

    }


    @Override
    public void sensor() {
        boolean flag = true;
        int i = 0;
        // start checking for new beliefs and changes , any change make a new belief.
        // and this will be using certain methods in the environment , and it's not fixed , it depends.
        // means there is not a single method to deal with that.
        // check location and storage permission , is playing the role of observing environment current state!
        //here! this agent represent permissions beliefs as classes that holds boolean attributes to represent
        // permission state in the environment whether it's granted or not , so it sense the it's environment,
        // to check permission state (state as term not as [technical term in MAS field]).
        // then it update beliefSet
        //  updating beliefs values.
        while (flag) {

            boolean locationBoolean = getLocalEnvironment().checkLocationPermission();
            boolean storageBoolean = getLocalEnvironment().checkStoragePermission();
            // if (locationBoolean && storageBoolean) break;
            //Initial beliefSet
            //Getting beliefs through environment interface
            //because we don't have a single interface to observe the whole environment, so we need to use only the interface,
            //that environment provide to use.

            LocationPermission locationPermission = new LocationPermission(false);
            Belief<LocationPermission> locationPermissionBelief = new Belief<>("locationPermissionBelief", locationPermission);
            StoragePermission storagePermission = new StoragePermission(false);
            Belief<StoragePermission> storagePermissionBelief = new Belief<>("storagePermissionBelief", storagePermission);

            locationPermissionBelief.getBeliefValue().setGranted(locationBoolean);
            storagePermissionBelief.getBeliefValue().setGranted(storageBoolean);

            // updating beliefs Set through belief revision function.
            beliefRevisionFunction(getBeliefSet(), locationPermissionBelief);
            SystemClock.sleep(5000);
            Log.d(TAG, "In Agent Looper Thread: # brf done added to beliefSet : locationPermission state : " + locationBoolean + " ##" + i);
            SystemClock.sleep(500);
            beliefRevisionFunction(getBeliefSet(), storagePermissionBelief);
            Log.d(TAG, "In Agent Looper Thread: # brf done added to beliefSet: storagePermission state : " + storageBoolean + " ##" + i);
            SystemClock.sleep(500);
            optionsGenerationFunction(getBeliefSet(), getDesireSet());
            Log.d(TAG, "In Agent Looper Thread: #  ogf done : beliefs set state : " + getBeliefSet().toString() + " ##" + i);
            SystemClock.sleep(500);
            Desire desire = filter(getBeliefSet(), getDesireSet());
            // if there is no more desires to achieve break the loop;

//            Log.d(TAG, "In Agent Looper Thread: # filter done : Desire state : " + desire.getName() + " " + desire.getCurrentState() + " ##" + i);
            SystemClock.sleep(500);
            plan(getBeliefSet(), desire);
            i++;
//            locationBoolean = getLocalEnvironment().checkLocationPermission();
//            if (!locationBoolean) {
//                desire.setCurrentState("new");
//            }
//            storageBoolean = getLocalEnvironment().checkStoragePermission();
//            if (!storageBoolean) {
//                desire.setCurrentState("new");
//            }
            Log.d(TAG, "In Agent Looper Thread: #  plan done : locationBoolean new state : " + locationBoolean + " ##" + i);
            Log.d(TAG, "In Agent Looper Thread: #  plan done : storageBoolean new state : " + storageBoolean + " ##" + i);
            if (desire == null) {
                break;
            }
        }

        // checkPermissions() == true , means a new belief

        // handleLocationPermissionPlan.execute();
        //getLocalEnvironment().toaster();
    }

    @Override
    public void effector() {

    }

    @Override
    public void run() {
        // Looper.prepare();
        // dataManagerAgentHandler = new Handler();
        sensor();
        //  Looper.loop();
    }

    //in my opinion  the return type is not necessary , bcz the void already can change the current belief set
    // in Woldridge book , BDI Reasoning cycle said that it return belief set type.
    //based on the environment state, the agent will choose a goal to achieve.
    @Override
    public void beliefRevisionFunction(HashMap<String, Belief> beliefSet, Belief belief) {
        /*brf affecting in essence on belief base*/
        //check contradiction --> there is no need, it will be just code redundancy;
        // we can do it just be rechecking permission again and compare it with belief value
        // eg , if the permission is really granted or not

        if (!beliefSet.containsKey(belief.getBeliefName())) {
            beliefSet.put(belief.getBeliefName(), belief);
        } else {
            //updating beliefs
            //updating locationPermissionBelief by replacing the old value with the new one!
            if (belief.getBeliefName().equals("locationPermissionBelief")) {
                LocationPermission lp = ((LocationPermission) beliefSet.get("locationPermissionBelief").getBeliefValue());
                Belief oldBelief = beliefSet.get("locationPermissionBelief");
                //hashmap replacing function working only for api version 24 and above
                beliefSet.replace("locationPermissionBelief", oldBelief, belief);
            }
            if (belief.getBeliefName().equals("storagePermissionBelief")) {
                StoragePermission sp = ((StoragePermission) beliefSet.get("storagePermissionBelief").getBeliefValue());
                Belief oldBelief = beliefSet.get("storagePermissionBelief");
                beliefSet.replace("storagePermissionBelief", oldBelief, belief);
            }
        }

    }

    @Override
    public void optionsGenerationFunction(HashMap<String, Belief> beliefSet, HashMap<String, Desire> desireSet) {
        /*in essence it affecting on desire base and desires state*/
        //it makes changes on desires set and choose the plans to achieve it
        //For example, if the agent believes that the location permission has not been granted,
        //then it can generate an option (plan) to request the location permission.
        for (Map.Entry<String, Belief> set : beliefSet.entrySet()) {
            if (set.getValue().getBeliefValue() instanceof LocationPermission) {
                LocationPermission locationPermission = (LocationPermission) set.getValue().getBeliefValue();
                //if permission is granted then set the desire state to finished. means goal is already achieved!
                if (locationPermission.isGranted())
                    desireSet.get("grantLocationPermissionDesire").setCurrentState(DesireState.ACHIEVED);
                else {
                    desireSet.get("grantLocationPermissionDesire").setCurrentState(DesireState.NEW);
                    getPlans().put(handleLocationPermissionPlan.getName(), handleLocationPermissionPlan);
                }
            } else if (set.getValue().getBeliefValue() instanceof StoragePermission) {
                StoragePermission storagePermission = (StoragePermission) set.getValue().getBeliefValue();
                //if permission is granted then set the desire state to finished. means goal is already achieved!
                if (storagePermission.isGranted())
                    desireSet.get("grantStoragePermissionDesire").setCurrentState(DesireState.ACHIEVED);
                else {
                    //setting the desire state to new and put the according plan to plansSet.
                    desireSet.get("grantStoragePermissionDesire").setCurrentState(DesireState.NEW);
                    getPlans().put(handleStoragePermissionPlan.getName(), handleStoragePermissionPlan);
                }
            }
        }

    }

    @Override
    public Desire filter(HashMap<String, Belief> beliefSet, HashMap<String, Desire> desireSet) {
        // choosing one goal to commit
        // the returned desire represent the intention
        int priority = 0;
        for (Map.Entry<String, Desire> set : desireSet.entrySet()) {
            if (priority == set.getValue().getPriority() && (set.getValue().getCurrentState() == DesireState.NEW)) {
                //change state to adopted.
                set.getValue().setCurrentState(DesireState.ADOPTED);
                return set.getValue();
            }
            priority++;
        }
        return null;
    }

    //this method will choose the right plan to execute from plans repository
    @Override
    public void plan(HashMap<String, Belief> beliefSet, Desire committedGoal) {
        if (committedGoal == null) {
            Plan switchEnvironment = new Plan("HandleSwitchActivity", null, this, getEnvironment()) {
                @Override
                public void execute(Object... args) {
                    getLocalEnvironment().startActivity(new Intent(getLocalEnvironment(), LoginActivity.class));
                    getLocalEnvironment().finish();
                }

                @Override
                public boolean preConditions(Object... args) {
                    return false;
                }

                @Override
                public void postConditions(Object... args) {

                }
            };
            switchEnvironment.execute();
        }
        for (Map.Entry<String, Plan> set : this.getPlans().entrySet()) {
            if (set.getValue().getGoal().getName().equals(committedGoal.getName())) {
                try {
                    System.out.println(set.getValue().getName());
                    System.out.println(committedGoal.getName() + "will be executed");
                    set.getValue().execute();
                } catch (Exception e) {
                    System.out.println("ERRORORORORORORO");
                    e.printStackTrace();
                }
            }
        }
    }

    //just a prototype method for setting desires priority
    public PermissionActivity getLocalEnvironment() {
        return localEnvironment;
    }

    public void setLocalEnvironment(PermissionActivity localEnvironment) {
        this.localEnvironment = localEnvironment;
    }
    //
}
