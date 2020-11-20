//
//  EducationCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "EducationCell.h"
#import "BMSQ_MainTeactherCell.h"
#import "BMSQ_RecommendShopCell.h"


@interface EducationCell ()

@end

@implementation EducationCell

-(UITableViewCell *)setMainCell:(UITableView *)tableView indexPath:(NSIndexPath *)indexPath{
    
    
    //地址
    if (indexPath.section ==2) {
        static NSString *identifier = @"schooladdresscell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            UILabel *addLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 40)];
            addLabel.backgroundColor = [UIColor whiteColor];
            addLabel.tag = 100;
            [cell addSubview:addLabel];
            
            UIImageView *imageV  = [[UIImageView alloc]initWithFrame:CGRectMake(15, 20, 15, 20)];
            imageV.backgroundColor = [UIColor clearColor];
            [imageV setImage:[UIImage imageNamed:@"address"]];
            [cell addSubview:imageV];
            
            addLabel.text = @"";
            addLabel.textColor = APP_TEXTCOLOR;
            addLabel.font = [UIFont systemFontOfSize:14.f];
        }
        UILabel *label = [cell viewWithTag:100];
        label.text = [NSString stringWithFormat:@"            %@",[self.shopInfo objectForKey:@"street"]];
        return cell;
    }
    //最近访问
    else if (indexPath.section == 3){
        static NSString *identifier = @"schoolaccesscell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UILabel *addLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 40)];
            addLabel.backgroundColor = [UIColor whiteColor];
            [cell addSubview:addLabel];
            
            addLabel.text = @"    最近访问";
            addLabel.textColor = APP_TEXTCOLOR;
            addLabel.font = [UIFont systemFontOfSize:14.f];
            for (int i=0; i<3; i++) {
                UIImageView *headImage = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60-(i*25), 10+5, 20, 20)];
                headImage.backgroundColor = APP_NAVCOLOR;
                headImage.layer.borderColor = [APP_NAVCOLOR CGColor];
                headImage.layer.borderWidth = 0.6;
                headImage.layer.masksToBounds = YES;
                headImage.layer.cornerRadius = 6;
                headImage.tag = 1000+i;
                headImage.hidden = YES;
                [cell addSubview:headImage];
                
            }
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        UIImageView *image1= (UIImageView *)[cell viewWithTag:1000];
        UIImageView *image2= (UIImageView *)[cell viewWithTag:1001];
        UIImageView *image3= (UIImageView *)[cell viewWithTag:1002];
        

        
        if (self.recentVisitor.count>=1) {
            NSDictionary *dic = [self.recentVisitor objectAtIndex:0];
            image1.hidden = NO;
            [image1 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@""]];
        }
        if (self.recentVisitor.count>=2){
            NSDictionary *dic = [self.recentVisitor objectAtIndex:1];
            image2.hidden = NO;
            [image2 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@""]];
        }
        if (self.recentVisitor.count>=3){
            NSDictionary *dic = [self.recentVisitor objectAtIndex:2];
            image3.hidden = NO;
            [image3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"avatarUrl"]]] placeholderImage:[UIImage imageNamed:@""]];
        }
        
        
        return cell;
        
    }
    //名师堂
    else if (indexPath.section == 4){
        
        static NSString *identifier = @"schooldaystartcell";
        BMSQ_MainTeactherCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[BMSQ_MainTeactherCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
        }
        [cell setMainTeacher:self.shopTeacher];
        return cell;
        
    }
    //每日之星
    else if (indexPath.section == 5){
        static NSString *identifier = @"schoolaccesscell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UILabel *addLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
            addLabel.backgroundColor = [UIColor whiteColor];
            [cell addSubview:addLabel];
            
            addLabel.text = @"    每日(周,月)之星";
            addLabel.textColor = APP_TEXTCOLOR;
            addLabel.font = [UIFont systemFontOfSize:14.f];
            
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        return cell;
    }
    //每日之星内容
    else if (indexPath.section == 6){
        static NSString *identifier = @"textCellEducation";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            cell.backgroundColor = [UIColor clearColor];
            
            
            UIImageView *bgView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 100)];
            bgView.backgroundColor = [UIColor whiteColor];
            bgView.tag = 100;
            [cell addSubview:bgView];
            UILabel *label = [[UILabel alloc]init];
            label.textColor = APP_TEXTCOLOR;
            label.font = [UIFont systemFontOfSize:13];
            label.backgroundColor = [UIColor whiteColor];
            label.tag =200;
            label.numberOfLines = 0;
            [cell addSubview:label];
            
        }
        UIImageView *bgImage = [cell viewWithTag:100];
        NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.studentStar objectForKey:@"starUrl"]];
        [bgImage sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_noData"]];
        
        UILabel *label =[cell viewWithTag:200];
        NSString *starInfo=[self.studentStar objectForKey:@"starInfo"];
        label.text =starInfo;
        CGSize size = [starInfo boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-16, MAXFLOAT)
                                                            options:NSStringDrawingUsesLineFragmentOrigin
                                                         attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13]}
                                                            context:nil].size;
        label.frame = CGRectMake(0, 100, APP_VIEW_WIDTH, size.height+20);
        
        return cell;
    }
    //招生启示
    else if(indexPath.section == 7){
        static NSString *identifier = @"schoolaccesscell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UILabel *addLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
            addLabel.backgroundColor = [UIColor whiteColor];
            [cell addSubview:addLabel];
            
            addLabel.text = @"    招生启示";
            addLabel.textColor = APP_TEXTCOLOR;
            addLabel.font = [UIFont systemFontOfSize:14.f];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        return cell;
    }
    //招生启示内容
    else if(indexPath.section == 8){
        static NSString *identifier = @"textCellEducation";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            cell.backgroundColor = [UIColor clearColor];
            UIImageView *bgView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 100)];
            bgView.backgroundColor = [UIColor whiteColor];
            bgView.tag = 100;
            [cell addSubview:bgView];
        }
        UIImageView *bgImage = [cell viewWithTag:100];
        NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.shopRecruitInfo objectForKey:@"advUrl"]];
        [bgImage sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv_noData"]];
        
        return cell;
    }
    //推荐商家
    else if ( indexPath.section ==9){
        
        static NSString *cellIdentifier = @"BMSQ_ShopCellcell";
        BMSQ_RecommendShopCell *cell = (BMSQ_RecommendShopCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            cell = [[BMSQ_RecommendShopCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        }
      
        [cell setCellValue:[self.aroundShop objectAtIndex:indexPath.row]];
        
        return cell;

    }
    
    static NSString *identifier = @"";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
        
    }
    return cell;
    
    
}

+(UITableViewCell *)setClassInfo:(UITableView *)tableView class:(NSDictionary *)classDic left:(NSArray *)leftArray indexPath:(NSIndexPath *)indexPath  classTime:(NSString *)classTime classHeigh:(float)classHeigh{
    static NSString *identifier = @"classContent";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
        bgView.backgroundColor = [UIColor whiteColor];
        bgView.tag = 90;
        [cell addSubview:bgView];
        
        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 70, 40)];
        leftLabel.textColor = APP_TEXTCOLOR;
        leftLabel.font = [UIFont systemFontOfSize:13];
//        leftLabel.textAlignment = NSTextAlignmentCenter;
        leftLabel.backgroundColor = [UIColor clearColor];
        leftLabel.tag =100;
        [cell addSubview:leftLabel];
        
        UILabel *rightLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 0, APP_VIEW_WIDTH-90, 40)];
        rightLabel.textColor = APP_TEXTCOLOR;
        rightLabel.font = [UIFont systemFontOfSize:13];
        rightLabel.backgroundColor = [UIColor clearColor];
        rightLabel.numberOfLines = 0;
        rightLabel.tag =200;
        [cell addSubview:rightLabel];
        
    }
    UIView *bgView = [cell viewWithTag:90];
    bgView.frame =CGRectMake(0, 0, APP_VIEW_WIDTH, 40);
    
    UILabel *leftLabel = (UILabel *)[cell viewWithTag:100];
    leftLabel.text = [leftArray objectAtIndex:indexPath.row];
    leftLabel.frame =CGRectMake(10, 0, 70, 40);

    
    
    if (classDic.count>0) {
        UILabel *rightLabel = (UILabel *)[cell viewWithTag:200];
        rightLabel.frame =CGRectMake(80, 0, APP_VIEW_WIDTH-90, 40);
        switch (indexPath.row) {
            case 0:
            {
                rightLabel.text = [classDic objectForKey:@"className"];
 
            }
                break;
            case 1:   //学习时间
            {
//                rightLabel.text = [NSString stringWithFormat:@"%@至%@",[classDic objectForKey:@"learnStartDate"],[classDic objectForKey:@"learnEndDate"]];
//            
                
                rightLabel.text = classTime;
                
                leftLabel.frame = CGRectMake(10, 0, 70, classHeigh-1);
                rightLabel.frame = CGRectMake(80, 0, APP_VIEW_WIDTH-90,classHeigh-1);
                bgView.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, classHeigh-1);
                
            }
                break;
            case 2:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@",[classDic objectForKey:@"classTime"]];
                
            }
                break;
            case 3:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@",[classDic objectForKey:@"learnMemo"]];
                
            }
                break;
            case 4:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@元",[classDic objectForKey:@"learnFee"]];
                
            }
                break;
            case 5:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@",[classDic objectForKey:@"learnNum"]];
                
            }
                break;
            case 6:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@",[classDic objectForKey:@"teacherName"]];
                
            }
                break;
            case 7:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@至%@",[classDic objectForKey:@"signStartDate"],[classDic objectForKey:@"signEndDate"]];
                
            }
                break;
            case 8:
            {
                rightLabel.text = [NSString stringWithFormat:@"%@",[classDic objectForKey:@"classInfo"]];
                
            }
                break;
                
            default:
                break;
        }
        
        
    }
 
    
    return cell;

}



@end
