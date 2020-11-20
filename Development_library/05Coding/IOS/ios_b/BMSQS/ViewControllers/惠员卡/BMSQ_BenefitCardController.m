//
//  BMSQ_BenefitCardController.m
//  BMSQS
//  re
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BenefitCardController.h"
#import "BMSQ_CardRuleViewController.h"
#import "BMSQ_MemberCardListViewController.h"
#import "PNColor.h"
@interface BMSQ_BenefitCardController ()

@end

@implementation BMSQ_BenefitCardController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
    
    [self.navigationItem setTitle:[ud objectForKey:@"shopName"]];
//    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.leftBarButtonItem= nil;
    [_tableView setSeparatorColor:[UIColor clearColor]];
    
    [self customRightBtn];
    [self createModel];
//    [self countHttpReqeust];
//    [self httpRequest];
//    [self dataHttpReqeust];
//    [self moneydataHttpReqeust];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [self reloadCardData];
    NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
    [self.navigationItem setTitle:[ud objectForKey:@"shopName"]];
    [super viewWillAppear:animated];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
#pragma mark - Aciton
#pragma mark -CustomView
- (void)customRightBtn
{
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"设置", @"设置") style:UIBarButtonItemStylePlain target:self action:@selector(setupClicked) ];
    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = item;
    
}

- (void)setupClicked
{
    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_BenefitCard" bundle:nil];
    BMSQ_CardRuleViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_CardRule"];
    vc.cardDataArray = self.cardDataArray;
    vc.hidesBottomBarWhenPushed = YES;
    [self.tabBarController.tabBar setHidden:YES];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)createModel
{
    if(self.shopCountDic ==nil )
        self.shopCountDic = [[NSMutableDictionary alloc] init];

    if(self.cardDataArray ==nil )
        self.cardDataArray = [[NSMutableArray alloc] init];
    
    
    if(self.memberMonthDataArray==nil)
        self.memberMonthDataArray = [[NSMutableArray alloc] init];
    if(self.memberMonthArray==nil)
        self.memberMonthArray = [[NSMutableArray alloc] init];
    
    if(self.memberMoneyArray==nil)
        self.memberMoneyArray = [[NSMutableArray alloc] init];
    if(self.memberMoneyDataArray==nil)
        self.memberMoneyDataArray = [[NSMutableArray alloc] init];
    

}

#pragma mark -Delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 5 + self.cardDataArray.count + 2;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row<5){
        return 44.f;
    }
    else if(indexPath.row>=5&&indexPath.row<(5+self.cardDataArray.count)){
        return 88.f;
    }else if(indexPath.row>=(5+self.cardDataArray.count)){
        return 196.f;
    }
    return 44;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    if(indexPath.section==0){
        if(indexPath.row<5){
            cellIdentifier = @"MemberTableViewCell";
        }
        else if(indexPath.row>=5&&indexPath.row<(5+self.cardDataArray.count)){
            cellIdentifier = @"CardTableViewCell";
        }
        else if(indexPath.row>=5+self.cardDataArray.count){
            cellIdentifier = @"ChartTableViewCell";
        }
    }
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }else{
        for (UIView *view in setCell.contentView.subviews) {
            if(view.tag == 8800||view.tag==103){
                [view removeFromSuperview];
            }
        }
    }
    
    CGFloat cellHeight = 0.5;
    
    if(indexPath.section==0){
        setCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if(indexPath.row==0){
            UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
            titleLabel.text = @"总会员数";
            
            NSString *cStr = [NSString stringWithFormat:@"%d人",[[self.shopCountDic objectForKey:@"nbrOfVip"] intValue]];
            UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
            contentLabel.text = cStr?cStr:@"";
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(titleLabel.frame.origin.x, setCell.frame.size.height-cellHeight, setCell.contentView.frame.size.width, cellHeight)];
            view.backgroundColor = [UIColor lightGrayColor];
            view.tag = 8800;
            [setCell.contentView addSubview:view];
        }else if(indexPath.row==1){
            UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
            titleLabel.text = @"近一月新增数";
            
            UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
            NSString *cStr = [NSString stringWithFormat:@"%d人",[[self.shopCountDic objectForKey:@"nbrOfNewVip"] intValue]];
            contentLabel.text = cStr?cStr:@"";
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(titleLabel.frame.origin.x, setCell.frame.size.height-cellHeight, setCell.contentView.frame.size.width, cellHeight)];
            view.backgroundColor = [UIColor lightGrayColor];
            view.tag = 8800;
            [setCell.contentView addSubview:view];
        }else if(indexPath.row==2){
            UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
            titleLabel.text = @"总消费金额";
            
            UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
            NSString *cStr = [NSString stringWithFormat:@"%d元",[[self.shopCountDic objectForKey:@"amountOfConsumption"] intValue]];
            contentLabel.text = cStr?cStr:@"";
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(titleLabel.frame.origin.x, setCell.frame.size.height-cellHeight, setCell.contentView.frame.size.width, cellHeight)];
            view.backgroundColor = [UIColor lightGrayColor];
            view.tag = 8800;
            [setCell.contentView addSubview:view];
        }else if(indexPath.row==3){
            UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
            titleLabel.text = @"总积分";
            
            UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
            NSString *cStr = [NSString stringWithFormat:@"%d分",[[self.shopCountDic objectForKey:@"amountOfPoint"] intValue]];
            contentLabel.text = cStr?cStr:@"";
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(titleLabel.frame.origin.x, setCell.frame.size.height-cellHeight, setCell.contentView.frame.size.width, cellHeight)];
            view.backgroundColor = [UIColor lightGrayColor];
            view.tag = 8800;
            [setCell.contentView addSubview:view];
            
        }else if(indexPath.row==4){
            UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
            titleLabel.text = @"三月内到期积分";
            
            UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
            NSString *cStr = [NSString stringWithFormat:@"%d分",[[self.shopCountDic objectForKey:@"amountOfExpiringPoint"] intValue]];
            contentLabel.text = cStr?cStr:@"";
            
            UIView *view = [[UIView alloc] initWithFrame:CGRectMake(titleLabel.frame.origin.x, setCell.frame.size.height-cellHeight, setCell.contentView.frame.size.width, cellHeight)];
            view.backgroundColor = [UIColor lightGrayColor];
            view.tag = 8800;
            [setCell.contentView addSubview:view];
            
        }else if(indexPath.row>=5&&indexPath.row<(5+self.cardDataArray.count)){
            NSDictionary *dicTemp = [self.cardDataArray objectAtIndex:indexPath.row-5];
            UIImageView *imageView = (UIImageView *)[setCell viewWithTag:102];
            if([[dicTemp objectForKey:@"cardLvl"] intValue]==1){
                imageView.image = [UIImage imageNamed:@"Member_AgCard"];
            }else if([[dicTemp objectForKey:@"cardLvl"] intValue]==2){
                imageView.image = [UIImage imageNamed:@"Member_AuCard"];
            }else if([[dicTemp objectForKey:@"cardLvl"] intValue]==3){
                imageView.image = [UIImage imageNamed:@"Member_PtCard"];
            }
            
            UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:1031];
            NSString *cStr = [NSString stringWithFormat:@"%d人",[[dicTemp objectForKey:@"vipNbr"] intValue]];
            contentLabel.text = cStr?cStr:@"";
            
            UILabel *moneyLabel = (UILabel *)[setCell.contentView viewWithTag:1033];
            NSString *mStr = [NSString stringWithFormat:@"消费：%.2f元",[[dicTemp objectForKey:@"consumeAmountCount"] floatValue]];
            moneyLabel.text = mStr?mStr:@"";
            
            UILabel *scoreLabel = (UILabel *)[setCell.contentView viewWithTag:1032];
            NSString *sStr = [NSString stringWithFormat:@"积分：%d分",[[dicTemp objectForKey:@"points"] intValue]];
            scoreLabel.font = [UIFont systemFontOfSize:13.f];
            scoreLabel.text = sStr?sStr:@"";
            
        }else if(indexPath.row==5+self.cardDataArray.count){
            UILabel *titleLabel = (UILabel *)[setCell viewWithTag:302];
            titleLabel.text = @"会员增长走势图";
            
            if (IsNOTNullOrEmptyOfArray(self.memberMonthDataArray)) {
                self.barChart = (PNBarChart *)[setCell viewWithTag:301];
                self.barChart.backgroundColor = [UIColor clearColor];
                if(self.memberMonthArray.count==1&&[[self.memberMonthArray lastObject] intValue]==0)
                {
                    self.barChart.yLabels = @[@"0",@"1",@"2"];
                }
                self.barChart.yLabelFormatter = ^(CGFloat yValue){
                    CGFloat yValueParsed = yValue;
                    NSString *labelText = [NSString stringWithFormat:@"%0.f人",yValueParsed];
                    return labelText;
                };
                self.barChart.labelMarginTop = 5.0;
                self.barChart.showChartBorder = YES;
                [self.barChart setXLabels:self.memberMonthArray];
                [self.barChart setYValues:self.memberMonthDataArray];
                [self.barChart setStrokeColors:@[[UIColor colorWithHexString:@"0xc5000a"]]];
                self.barChart.isGradientShow = NO;
                self.barChart.isShowNumbers = NO;
                [self.barChart strokeChart];
            }
        }else if(indexPath.row==6+self.cardDataArray.count){
            UILabel *titleLabel = (UILabel *)[setCell viewWithTag:302];
            titleLabel.text = @"消费金额走势图";
            
            if (IsNOTNullOrEmptyOfArray(self.memberMoneyDataArray)) {
                self.barChart = (PNBarChart *)[setCell viewWithTag:301];
                self.barChart.backgroundColor = [UIColor clearColor];
                if(self.memberMoneyArray.count==1&&[[self.memberMoneyDataArray lastObject] intValue]==0)
                {
                    self.barChart.yLabels = @[@"0",@"1",@"2"];
                }
                self.barChart.yLabelFormatter = ^(CGFloat yValue){
                    CGFloat yValueParsed = yValue;
                    NSString *labelText = [NSString stringWithFormat:@"%0.2f元",yValueParsed];
                    return labelText;
                };
                self.barChart.labelMarginTop = 5.0;
                self.barChart.showChartBorder = YES;
                [self.barChart setXLabels:self.memberMoneyArray];
                [self.barChart setYValues:self.memberMoneyDataArray];
                [self.barChart setStrokeColors:@[[UIColor colorWithHexString:@"0xc5000a"]]];
                self.barChart.isGradientShow = NO;
                self.barChart.isShowNumbers = NO;
                [self.barChart strokeChart];
            }
        }
    }
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if(indexPath.row>=5&&indexPath.row<(5+self.cardDataArray.count)){
        NSDictionary *dicTemp = [self.cardDataArray objectAtIndex:indexPath.row-5];
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_BenefitCard" bundle:nil];
        BMSQ_MemberCardListViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_MemberCardList"];
        vc.cardCode = [dicTemp objectForKey:@"cardCode"];
        vc.cardTitle = [dicTemp objectForKey:@"cardName"];
        vc.hidesBottomBarWhenPushed = YES;
        [self.tabBarController.tabBar setHidden:YES];
        [self.navigationController pushViewController:vc animated:YES];

    }
            
}

- (void)reloadCardData
{
    [self.cardDataArray removeAllObjects];
    [self.memberMonthDataArray removeAllObjects];
    [self.memberMonthArray removeAllObjects];
    [self.memberMoneyArray removeAllObjects];
    [self.memberMoneyDataArray removeAllObjects];
    
    [self countHttpReqeust];
    [self httpRequest];
    [self dataHttpReqeust];
    [self moneydataHttpReqeust];
}
#pragma mark - HttpRequest
- (void)httpRequest
{
    NSString* vcode = [gloabFunction getSign:@"getGeneralCardStastics" strParams:[gloabFunction getShopCode]];

    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime": [gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"getGeneralCardStastics" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [self.cardDataArray removeAllObjects];

        if(IsNOTNullOrEmptyOfArray(responseObject)){
            [self.cardDataArray addObjectsFromArray:responseObject];
            [_tableView reloadData];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}

- (void)countHttpReqeust
{
    NSString* vcode = [gloabFunction getSign:@"countCard" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime": [gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"countCard" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            self.shopCountDic = responseObject;
            [_tableView reloadData];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}

- (void)dataHttpReqeust
{
    NSString* vcode = [gloabFunction getSign:@"listIncreasingTrend" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime": [gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"listIncreasingTrend" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfArray(responseObject)){
            [self.memberMonthDataArray removeAllObjects];
            [self.memberMonthArray removeAllObjects];

            for (NSDictionary *dic in responseObject) {
                [self.memberMonthArray addObject:[dic objectForKey:@"month"]];
                [self.memberMonthDataArray addObject:[dic objectForKey:@"nbr"]];
            }
            [_tableView reloadData];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}

- (void)moneydataHttpReqeust
{
    NSString* vcode = [gloabFunction getSign:@"listConsumeTrend" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime": [gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"listConsumeTrend" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(IsNOTNullOrEmptyOfArray(responseObject)){
            [self.memberMoneyArray removeAllObjects];
            [self.memberMoneyDataArray removeAllObjects];

            for (NSDictionary *dic in responseObject) {
                [self.memberMoneyArray addObject:[dic objectForKey:@"month"]];
                [self.memberMoneyDataArray addObject:[dic objectForKey:@"fee"]];
            }
            [_tableView reloadData];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        CSAlert(error.localizedDescription);
    }];
}
@end
